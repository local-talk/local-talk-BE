package com.localtalk.api.review.domain

import com.localtalk.domain.BaseImage
import jakarta.persistence.*

@Entity
@Table(name = "review_image")
class ReviewImage(

    fileName: String,
    filePath: String,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val review: Review

) : BaseImage(fileName, filePath) {

}
