package com.puj.admincenter.controller

import com.puj.admincenter.domain.concepts.Concept
import com.puj.admincenter.dto.concepts.ConceptsDto
import com.puj.admincenter.service.ConceptService

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.data.repository.*;
import javax.validation.Valid
import javax.servlet.http.HttpServletRequest
import java.security.Principal
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@CrossOrigin
@RequestMapping("/concepts")
@RestController
class ConceptController(val conceptService: ConceptService) {
    companion object {
        val logger = LoggerFactory.getLogger(ConceptController::class.java)!!
    }

    @GetMapping(
        consumes = ["application/json"],
        produces = ["application/json"]
    )
/*     @ApiOperation(
        value = "Get all concepts",
        httpMethod = "GET"
    ) */
    fun getAllConcepts(@RequestParam(value = "vocabularyId", required = false) vocabularyId : String?,
                       @RequestParam(value = "conceptId", required = false) conceptId : Int?,
                       @RequestParam(value = "domainId", required = false) domainId : String?,
                       @RequestParam(value = "shortDesc", required = false) shortDesc : String?,
                       @RequestHeader(value = "authorization", required = true) authorization : String,
                       @PageableDefault(size = 100)
                       pageable: Pageable): ResponseEntity<*>{
                    
    return conceptService.getAllConcepts(vocabularyId,
                                         conceptId,
                                         domainId,
                                         shortDesc,
                                         "abc",
                                         pageable)
    }

    @PostMapping(
        value = ["/create"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun create(@RequestBody @Valid createConceptDto: ConceptsDto, 
               @RequestHeader(value="authorization", required=true) authorization: String): ResponseEntity<*>
        = conceptService.create(createConceptDto)
    
    @PutMapping(
        value = ["/delete/{conceptId}"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun delete(@PathVariable conceptId: Int,
                @RequestHeader(value="authorization", required=true) authorization: String): ResponseEntity<*>
        = conceptService.delete(conceptId,
                              authorization)
/*         
        
        @PathVariable userId: Int,
                @RequestHeader(value="authorization", required=true) authorization: String): ResponseEntity<*>
        = userService.getById(userId,
                              authorization) */
/* 
    @PostMapping(
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun create(@RequestBody @Valid createUserDto: CreateUserDto, 
               @RequestHeader(value="authorization", required=true) authorization: String): ResponseEntity<*>
        = userService.create(createUserDto) */
}