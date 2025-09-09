package com.localtalk.api.review.domain

import com.localtalk.domain.HardDeleteBaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "review_image")
class ReviewImage(
    @Column(name = "review_image_key", nullable = false)
    val reviewImageKey: String,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val review: Review

) : HardDeleteBaseEntity()
