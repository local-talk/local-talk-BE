package com.localtalk.api.bookmark.domain

import com.localtalk.api.event.domain.Event
import com.localtalk.api.member.domain.Member
import com.localtalk.domain.HardDeleteBaseEntity
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(name = "bookmark",
    uniqueConstraints = [UniqueConstraint(columnNames = ["event_id", "member_id"])])
class Bookmark (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val event: Event,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val member: Member,

    ) : HardDeleteBaseEntity(){
}
