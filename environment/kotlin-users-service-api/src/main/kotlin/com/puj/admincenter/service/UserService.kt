package com.puj.admincenter.service

import com.puj.admincenter.domain.users.User
import com.puj.admincenter.dto.users.UserDto
import com.puj.admincenter.dto.users.CreateUserDto
import com.puj.admincenter.dto.users.ModifyUserDto
import com.puj.admincenter.dto.IdResponseDto
import com.puj.admincenter.dto.login.TokenDto
import com.puj.admincenter.repository.users.UserRepository

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import java.util.stream.Collectors
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {
    companion object {
        val LOG = LoggerFactory.getLogger(UserService::class.java)!!
    }

    @Value(value = "\${jwt.secret}")
    private val jwtSecret: String? = null

    @Value(value = "\${jwt.expiration:5}")
    private val jwtExpiration: Long = 5

    fun count(): Long {
        return userRepository.count()
    }

    fun getById(userId: Int,
                authorization: String): ResponseEntity<*> {

        val user = userRepository.findById(userId)  // Hace solo el query
        return if (user.isPresent()) {
            ResponseEntity.ok(UserDto.convert(user.get()))
        } else {
            ResponseEntity<Any>(HttpStatus.NOT_FOUND)
        }
    }

    fun create(createUserDto: CreateUserDto): ResponseEntity<*> {
        if (userRepository.existsByEmail(createUserDto.email)) {
            val messageError = "User with email: ${createUserDto.email} already exists."
            LOG.error(messageError)
            return ResponseEntity<Any>(messageError,
                                       HttpStatus.CONFLICT)
        }
        val encodedPassword = BCrypt.hashpw(createUserDto.password,BCrypt.gensalt());
        val user = User(email = createUserDto.email,
                        name = createUserDto.name,
                        password = encodedPassword,
                        username = createUserDto.username)
        val userSaved = userRepository.save(user)
        LOG.info("User ${createUserDto.email} created with id ${userSaved.id}")

        val responseDto = IdResponseDto(userSaved.id.toLong())
        return ResponseEntity<IdResponseDto>(responseDto,
                                             HttpStatus.CREATED)
    }

    fun modify(modifyUserDto: ModifyUserDto): ResponseEntity<*> {
        val encodedPassword = userRepository.findPasswordByUser(modifyUserDto.username)
        return if(encodedPassword != null){
            val statusPassword = BCrypt.checkpw(modifyUserDto.previousPassword,encodedPassword);
            print(encodedPassword + "\n");
            print(statusPassword);
            return if (statusPassword)
            {
                val user = userRepository.findUserByUserAndPassword(modifyUserDto.username,
                                                                encodedPassword)
                return if (user != null) {

                    LOG.info("found user $user")
                    val jwtToken = getJWTToken(modifyUserDto.username)
            
                    println("tokenJwt: $jwtToken")

                    val token =  TokenDto(jwtToken,
                                        user.id)
                    LOG.info("Token $token for user $user generated")

                    val encodedPassword = BCrypt.hashpw(modifyUserDto.newPassword,BCrypt.gensalt());
                    val userSaved = userRepository.updatePasswordByUser(modifyUserDto.username, encodedPassword)
                    ResponseEntity<TokenDto>(token,
                                            HttpStatus.OK)


                } else {
                    val message = "the user does not exist or is not enabled" 
                    LOG.error(message)
                    ResponseEntity<String>(message,
                                        HttpStatus.NOT_FOUND)
                }
            }
            else {
                    val message = "the password provided for this user is not valid" 
                    LOG.error(message)
                    ResponseEntity<String>(message,
                                        HttpStatus.NOT_FOUND)
                }
            }
        else {
                val message = "the user does not exist or is not enabled" 
                LOG.error(message)
                ResponseEntity<String>(message,
                                    HttpStatus.NOT_FOUND)
            }
    }

    fun getJWTToken(username:String): String {
        val secretKey = "mySecretKey"
        val grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")
        val claims = Jwts.claims()
                         .setSubject(username)
        claims.put("identity", username)
        val token = Jwts.builder()
                        .setId("softtekJWT")
                        .setClaims(claims)
                        .claim("authorities", 
                               grantedAuthorities.stream()
                                                 .map(GrantedAuthority::getAuthority)
                                                 .collect(Collectors.toList()))
                        .setIssuedAt(Date(System.currentTimeMillis()))
                        .setExpiration(Date(System.currentTimeMillis() + 600000))
                        .signWith(SignatureAlgorithm.HS512,
                                  secretKey.toByteArray()).compact()
        return "Bearer " + token
    }
}