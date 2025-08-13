package com.localtalk.utils

import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Table
import jakarta.transaction.Transactional
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class JpaDatabaseCleaner(
    @PersistenceContext private val entityManager: EntityManager,
) : InitializingBean {
    private val tableNames = mutableListOf<String>()

    override fun afterPropertiesSet() {
        entityManager.metamodel.entities.filter { entity -> entity.javaType.getAnnotation(Entity::class.java) != null }
            .map { entity -> entity.javaType.getAnnotation(Table::class.java).name }.forEach { tableNames.add(it) }
    }

    @Transactional
    fun truncateAllTables() {
        entityManager.flush()
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate()
        tableNames.forEach { table ->
            entityManager.createNativeQuery("TRUNCATE TABLE `$table`").executeUpdate()
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate()
    }

}
