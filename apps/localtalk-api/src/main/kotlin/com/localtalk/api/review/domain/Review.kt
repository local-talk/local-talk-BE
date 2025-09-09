package com.localtalk.api.review.domain

import com.localtalk.api.event.domain.Event
import com.localtalk.api.member.domain.Member
import com.localtalk.domain.SoftDeleteBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "review")
class Review(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val event: Event,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val member: Member,

    @Column(name = "rating", nullable = false)
    val rating: Int,

    @Column(name = "comment", columnDefinition = "TEXT")
    var comment: String? = null,

) : SoftDeleteBaseEntity(){

    init {
        require(rating in 1..5) { "별점은 1~5 사이여야 합니다." }

        comment?.let {
            require(it.length <= 300) { "댓글은 300자 이하여야 합니다." }
        }
    }
}
