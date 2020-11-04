package com.puj.admincenter.service

import com.puj.admincenter.domain.concepts.Concept
import com.puj.admincenter.dto.concepts.ConceptDto
import com.puj.admincenter.dto.IdResponseDto
import com.puj.admincenter.repository.concepts.ConceptRepository
import com.puj.admincenter.repository.concepts.ConceptSpecification

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.util.*

@Service
class ConceptService(private val conceptRepository: ConceptRepository) {
    companion object {
        val LOG = LoggerFactory.getLogger(ConceptService::class.java)!!
    }

/*     fun count(): Long {
        return conceptRepository.count()
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
     */
    fun getAllConcepts(vocabularyId : String?,
                        conceptId : Int?,
                        domainId : String?,
                        shortDesc : String?,
                        authorization : String,
                        pageable : Pageable): ResponseEntity<*> {
            val concepts = conceptRepository.findAll(ConceptSpecification(vocabularyId,
                                                                        conceptId,
                                                                        domainId,
                                                                        shortDesc),
                                                    pageable)
            return if (concepts != null) {
                ResponseEntity.ok(concepts)
            } else {
                LOG.debug("No concepts found")
                ResponseEntity<Any>(HttpStatus.NOT_FOUND)
            }
        }

    fun create(createConceptDto: ConceptDto): ResponseEntity<*> {
        val fieldDeletedValue = 0
        val fieldExistsValue = 1
        if (conceptRepository.existsByConceptId(createConceptDto.conceptId)) {
            val messageError = "Concept with conceptId: ${createConceptDto.conceptId} already exists."
            LOG.error(messageError)
            return ResponseEntity<Any>(messageError,
                                       HttpStatus.CONFLICT)
        }
        val newIdHybrid = createConceptDto.code + createConceptDto.conceptId + createConceptDto.vocabularyId
        val concept = Concept(idHybrid = newIdHybrid,
                        pxordx = createConceptDto.pxordx,
                        oldpxordx = createConceptDto.oldpxordx,
                        codetype = createConceptDto.codetype,
                        conceptClassId = createConceptDto.conceptClassId,
                        conceptId = createConceptDto.conceptId.toInt(),
                        vocabularyId = createConceptDto.vocabularyId,
                        domainId = createConceptDto.domainId,
                        track = createConceptDto.track,
                        standardConcept = createConceptDto.standardConcept,
                        code = createConceptDto.code,
                        codewithperiods = createConceptDto.codewithperiods,
                        codescheme = createConceptDto.codescheme,
                        longDesc = createConceptDto.longDesc,
                        shortDesc = createConceptDto.shortDesc,
                        codeStatus = createConceptDto.codeStatus,
                        codeChange = createConceptDto.codeChange,
                        codeChangeYear = createConceptDto.codeChangeYear,
                        codePlannedType = createConceptDto.codePlannedType,
                        codeBillingStatus = createConceptDto.codeBillingStatus,
                        codeCmsClaimStatus = createConceptDto.codeCmsClaimStatus,
                        sexCd = createConceptDto.sexCd,
                        anatOrCond = createConceptDto.anatOrCond,
                        poaCodeStatus = createConceptDto.poaCodeStatus,
                        poaCodeChange = createConceptDto.poaCodeChange,
                        poaCodeChangeYear = createConceptDto.poaCodeChangeYear,
                        validStartDate = createConceptDto.validStartDate,
                        validEndDate = createConceptDto.validEndDate,
                        invalidReason = createConceptDto.invalidReason,
                        createDt = createConceptDto.createDt,
                        conceptState = 1)
        val conceptSaved = conceptRepository.save(concept)

        /* val concept = conceptRepository.createConceptByConceptId(createConceptDto.idHybrid,createConceptDto.pxordx, createConceptDto.oldpxordx,
        createConceptDto.codetype, createConceptDto.conceptClassId, createConceptDto.conceptId.toInt(), createConceptDto.vocabularyId, 
        createConceptDto.domainId, createConceptDto.track, createConceptDto.standardConcept, createConceptDto.code, createConceptDto.codewithperiods,
        createConceptDto.codescheme, createConceptDto.longDesc, createConceptDto.shortDesc, createConceptDto.codeStatus, createConceptDto.codeChange,
        createConceptDto.codeChangeYear, createConceptDto.codePlannedType,createConceptDto.codeBillingStatus,createConceptDto.codeCmsClaimStatus,
        createConceptDto.sexCd,createConceptDto.anatOrCond, createConceptDto.poaCodeStatus,createConceptDto.poaCodeChange,createConceptDto.poaCodeChangeYear,
        createConceptDto.validStartDate, createConceptDto.validEndDate, createConceptDto.invalidReason, createConceptDto.createDt.toInt(),"exists") */
/*         LOG.info("Concept ${createConceptDto.conceptId} created with id ${conceptSaved.id}")*/

        val responseDto = IdResponseDto(conceptSaved.id.toLong())
        return ResponseEntity<IdResponseDto>(responseDto,
                                             HttpStatus.CREATED)
       /*  return ResponseEntity.ok(concept) */
    }


    fun delete(conceptId: Int,
                authorization: String): ResponseEntity<*> {
        val fieldDeletedValue = 0
        val fieldExistsValue = 1
        if (conceptRepository.existsLogicallyByConceptId(conceptId,fieldExistsValue)) {
            conceptRepository.deleteConceptByConceptId(conceptId,fieldDeletedValue) 
            val concept = conceptRepository.selectConceptByConceptId(conceptId)
            return if (concept != null) {
                val message =  "Concept with conceptId: ${conceptId} was deleted."
                LOG.info(message)
                ResponseEntity.ok(message)
            } else {
                ResponseEntity<Any>(HttpStatus.NOT_FOUND)
            }
        }
        else {
            val messageError = "Concept with conceptId: ${conceptId} doesn't exist."
            LOG.error(messageError)
            return ResponseEntity<Any>(messageError,
                                       HttpStatus.CONFLICT)
        }
    }

    fun modify(modifyConceptDto: ConceptDto,authorization: String): ResponseEntity<*> {
        val fieldDeletedValue = 0
        val fieldExistsValue = 1
        if (!conceptRepository.existsLogicallyByConceptId(modifyConceptDto.conceptId,fieldExistsValue)) {
            val messageError = "Concept with conceptId: ${modifyConceptDto.conceptId} doesn't exist."
            LOG.error(messageError)
            return ResponseEntity<Any>(messageError,
                                       HttpStatus.CONFLICT)
        }
        val newIdHybrid = modifyConceptDto.code + modifyConceptDto.conceptId + modifyConceptDto.vocabularyId
        val concept = conceptRepository.updateConceptByConceptId(modifyConceptDto,newIdHybrid)
        val message = "Concept with conceptId ${modifyConceptDto.conceptId} was updated"
        LOG.info(message)

        return ResponseEntity.ok(message)
    }
}