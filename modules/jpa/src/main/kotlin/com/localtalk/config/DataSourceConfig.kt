package com.localtalk.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.master")
    fun mysqlHikariMasterConfig(): HikariConfig = HikariConfig()

    @Bean
    @Primary
    fun mysqlMasterDataSource(
        @Qualifier("mysqlHikariMasterConfig") hikariConfig: HikariConfig,
    ): DataSource = HikariDataSource(hikariConfig)
}
