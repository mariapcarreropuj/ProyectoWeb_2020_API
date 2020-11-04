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

/*     @Transactional
    @Modifying
    @Query("""
        INSERT INTO Concept concept (concept.idHybrid, concept.pxordx, concept.oldpxordx, concept.codetype, concept.conceptClassId, concept.conceptId, concept.vocabularyId, concept.domainId, concept.track, concept.standardConcept,
        concept.code, concept.codewithperiods, concept.codescheme, concept.longDesc, concept.shortDesc, concept.codeStatus, concept.codeChange, concept.codeChangeYear, concept.codePlannedType, concept.codeBillingStatus, concept.codeCmsClaimStatus, concept.sexCd,   concept.anatOrCond,
        concept.poaCodeStatus, concept.poaCodeChange, concept.poaCodeChangeYear,  concept.validStartDate, concept.validEndDate, concept.invalidReason, concept.createDt, concept.conceptState) VALUES (:idHybrid, :pxordx,  :oldpxordx, :codetype, :conceptClassId, :conceptId, :vocabularyId, :domainId, :track, :standardConcept, :code, :codewithperiods, :codescheme, :longDesc, :shortDesc, :codeStatus, :codeChange, :codeChangeYear,  :codePlannedType, :codeBillingStatus, :codeCmsClaimStatus, :sexCd, :anatOrCond,
   :poaCodeStatus, :poaCodeChange, :poaCodeChangeYear, :validStartDate, :validEndDate, :invalidReason, :createDt, :conceptState)""")
    fun createConceptByConceptId(idHybrid: String, pxordx: String, oldpxordx: String?, codetype: String,
    conceptClassId: String?, conceptId: Int, vocabularyId: String, domainId: String, track: String?, standardConcept: String?, code: String,
    codewithperiods: String?, codescheme: String?, longDesc: String?, shortDesc: String?, codeStatus: String?, codeChange: String?,
    codeChangeYear: String?, codePlannedType: String?, codeBillingStatus: String?, codeCmsClaimStatus: String?, sexCd: String?, anatOrCond: String?,
    poaCodeStatus: String?, poaCodeChange: String?, poaCodeChangeYear: String?, validStartDate: String?, validEndDate: String?, invalidReason: String?,
     createDt: Int?, conceptState: String): Int
 */
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

/* class ConceptSpecification(private val vocabularyId : String?,
                           private val conceptId : Int?,
                           private val domainId : String?,
                           private val shortDesc : String?) : Specification<Concept> {
    override fun toPredicate(root : Root<Concept>, query : CriteriaQuery<*>, cb : CriteriaBuilder): Predicate {
        val p = mutableListOf<Predicate>()
        vocabularyId?.let {p.add(cb.equal(root.get<String>("vocabularyId"), vocabularyId)) }
        conceptId?.let {p.add(cb.equal(root.get<Int>("conceptId"), conceptId)) }
        domainId?.let {p.add(cb.equal(root.get<String>("domainId"), domainId)) }
        shortDesc?.let {p.add(cb.like(root.get<String>("shortDesc"), "%$shortDesc%")) }
        return cb.and(*p.toTypedArray())
    }
} */