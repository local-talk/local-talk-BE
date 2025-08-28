package com.localtalk.api.event.domain

import com.localtalk.domain.BaseImage
import jakarta.persistence.*

@Entity
@Table(name = "event_image")
class EventImage(
    fileName: String,
    filePath: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    val event: Event

) : BaseImage(fileName, filePath) {

}
