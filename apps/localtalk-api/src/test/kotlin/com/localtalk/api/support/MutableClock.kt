package com.localtalk.api.support

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

class MutableClock(
    private var current: Instant,
    private val zone: ZoneId = ZoneOffset.UTC,
) : Clock() {

    override fun withZone(zone: ZoneId): Clock = MutableClock(current, zone)
    override fun getZone(): ZoneId = zone
    override fun instant(): Instant = current

    fun now() {
        this.current = Instant.now()
    }

    fun set(instant: Instant) {
        current = instant
    }

    fun minusSeconds(sec: Long) {
        current = current.minusSeconds(sec)
    }
}
