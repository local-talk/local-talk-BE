package com.localtalk.domain

import com.localtalk.internal.InternalBaseEntityApi
import jakarta.persistence.MappedSuperclass

@OptIn(InternalBaseEntityApi::class)
@MappedSuperclass
abstract class HardDeleteBaseEntity : BaseEntity()
