package com.puj.admincenter.repository.concepts

import com.puj.admincenter.domain.concepts.Concept
import com.puj.admincenter.dto.concepts.ConceptDto
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.*;
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Repository
interface ConceptRepository : JpaRepository<Concept, Int>,
                           JpaSpecificationExecutor<Concept> {

    @Query("""
        SELECT COUNT(conceptId) > 0
        FROM Concept concept
        WHERE concept.conceptId = :conceptId
    """)
    fun existsByConceptId(@Param("conceptId") conceptId: Int): Boolean

    @Query("""
        SELECT COUNT(conceptId) > 0
        FROM Concept concept
        WHERE concept.conceptId = :conceptId AND concept.conceptState = :FieldUpdateValue
    """)
    fun existsLogicallyByConceptId(conceptId: Int, FieldUpdateValue: Int): Boolean

        @Transactional
        @Modifying
        @Query("""
        UPDATE Concept concept SET conceptState = :FieldUpdateValue
        WHERE concept.conceptId = :conceptId
    """)
    fun deleteConceptByConceptId(conceptId: Int, FieldUpdateValue: Int): Int

    @Transactional
    @Modifying
    @Query("""
        UPDATE Concept concept 
        SET idHybrid = :#{#idHybrid},
        pxordx = :#{#modifyConceptDto.pxordx},
        oldpxordx = :#{#modifyConceptDto.oldpxordx},
        codetype = :#{#modifyConceptDto.codetype},
        conceptClassId = :#{#modifyConceptDto.conceptClassId},
        conceptId = :#{#modifyConceptDto.conceptId},
        vocabularyId = :#{#modifyConceptDto.vocabularyId},
        domainId = :#{#modifyConceptDto.domainId},
        track = :#{#modifyConceptDto.track},
        standardConcept = :#{#modifyConceptDto.standardConcept},
        code = :#{#modifyConceptDto.code},
        codewithperiods = :#{#modifyConceptDto.codewithperiods},
        codescheme = :#{#modifyConceptDto.codescheme},
        longDesc = :#{#modifyConceptDto.longDesc},
        shortDesc = :#{#modifyConceptDto.shortDesc},
        codeStatus = :#{#modifyConceptDto.codeStatus},
        codeChange = :#{#modifyConceptDto.codeChange},
        codeChangeYear = :#{#modifyConceptDto.codeChangeYear},
        codePlannedType = :#{#modifyConceptDto.codePlannedType},
        codeBillingStatus = :#{#modifyConceptDto.codeBillingStatus},
        codeCmsClaimStatus = :#{#modifyConceptDto.codeCmsClaimStatus},
        sexCd = :#{#modifyConceptDto.sexCd},
        anatOrCond = :#{#modifyConceptDto.anatOrCond},
        poaCodeStatus = :#{#modifyConceptDto.poaCodeStatus},
        poaCodeChange = :#{#modifyConceptDto.poaCodeChange},
        poaCodeChangeYear = :#{#modifyConceptDto.poaCodeChangeYear},
        validStartDate = :#{#modifyConceptDto.validStartDate},
        validEndDate = :#{#modifyConceptDto.validEndDate},
        invalidReason = :#{#modifyConceptDto.invalidReason},
        createDt = :#{#modifyConceptDto.createDt}
        WHERE concept.conceptId = :#{#modifyConceptDto.conceptId}
    """)
    fun updateConceptByConceptId(modifyConceptDto: ConceptDto, idHybrid: String): Int

    @Query("""
        SELECT concept
        FROM Concept concept
        WHERE concept.conceptId = :conceptId
    """)
    fun selectConceptByConceptId(conceptId: Int): Concept

    
}
