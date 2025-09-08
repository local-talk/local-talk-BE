package com.localtalk.api.visit.domain

import com.localtalk.api.event.domain.Event
import com.localtalk.api.member.domain.Member
import com.localtalk.domain.HardDeleteBaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "visit",
    uniqueConstraints = [UniqueConstraint(columnNames = ["event_id", "member_id"])])
class Visit(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val event: Event,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val member: Member
) : HardDeleteBaseEntity()
