package com.localtalk.api.event.domain

import com.localtalk.api.member.domain.Member
import com.localtalk.domain.SoftDeleteBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "event")
class Event (
    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "event_image_key", nullable = false)
    val eventImageKey: String,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDate,

    @Column(name = "end_date")
    val endDate: LocalDate? = null,

    @Column(name = "operating_hours")
    val operatingHours: String? = null,

    @Column(length = 500)
    val location: String? = null,

    @Column(columnDefinition = "TEXT")
    val address: String? = null,

    @Column(name = "price")
    val price: Int,

    @Column(name = "contact_phone")
    val contactPhone: String? = null,

    @Column(name = "official_website")
    val officialWebsite: String? = null,

    @Column(precision = 10, scale = 8)
    val latitude: BigDecimal? = null,

    @Column(precision = 11, scale = 8)
    val longitude: BigDecimal? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val member: Member,

) : SoftDeleteBaseEntity()
