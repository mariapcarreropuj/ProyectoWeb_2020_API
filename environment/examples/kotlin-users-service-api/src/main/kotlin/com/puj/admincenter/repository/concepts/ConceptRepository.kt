package com.puj.admincenter.repository.concepts

import com.puj.admincenter.domain.concepts.Concept

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
    fun existsLogicallyByConceptId(conceptId: Int, FieldUpdateValue: String): Boolean

        @Transactional
        @Modifying
        @Query("""
        UPDATE Concept concept SET conceptState = :FieldUpdateValue
        WHERE concept.conceptId = :conceptId
    """)
    fun deleteConceptByConceptId(conceptId: Int, FieldUpdateValue: String): Int

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