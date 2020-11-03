package com.puj.admincenter.domain.concepts

import org.hibernate.annotations.GenericGenerator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "concepts")
data class Concept(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false, unique = true)
    val idHybrid: String? = "",

    @Column(nullable = false)
    val pxordx: String = "",

    @Column(nullable = true)
    val oldpxordx: String? = "",

    @Column(nullable = false)
    val codetype: String = "",

    @Column(nullable = true)
    val conceptClassId: String? = "",

    @Column(nullable = true)
    val conceptId: Int? = 0,

    @Column(nullable = false)
    val vocabularyId: String = "",

    @Column(nullable = false)
    val domainId: String = "",

    @Column(nullable = true)
    val track: String? = "",

    @Column(nullable = true)
    val standardConcept: String? = "",

    @Column(nullable = false)
    val code: String = "",

    @Column(nullable = true)
    val codewithperiods: String? = "",

    @Column(nullable = true)
    val codescheme:  String? = "",

    @Column(nullable = true)
    val longDesc:  String? = "",

    @Column(nullable = true)
    val shortDesc:  String? = "",

    @Column(nullable = true)
    val codeStatus:  String? = "",

    @Column(nullable = true)
    val codeChange:  String? = "",

    @Column(nullable = true)
    val codeChangeYear:  String? = "",

    @Column(nullable = true)
    val codePlannedType:  String? = "",

    @Column(nullable = true)
    val codeBillingStatus:  String? = "",

    @Column(nullable = true)
    val codeCmsClaimStatus:  String? = "",

    @Column(nullable = true)
    val sexCd:  String? = "",

    @Column(nullable = true)
    val anatOrCond:  String? = "",

    @Column(nullable = true)
    val poaCodeStatus:  String? = "",

    @Column(nullable = true)
    val poaCodeChange:  String? = "",

    @Column(nullable = true)
    val poaCodeChangeYear:  String? = "",

    @Column(nullable = true)
    val validStartDate:  String? = "",

    @Column(nullable = true)
    val validEndDate:  String? = "",

    @Column(nullable = true)
    val invalidReason:  String? = "",

    @Column(nullable = true)
    val createDt: Int? = 0,

    @Column(nullable = false)
    val conceptState: Int = 1
)