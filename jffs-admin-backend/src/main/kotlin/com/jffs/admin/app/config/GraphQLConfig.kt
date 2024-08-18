package com.jffs.admin.app.config

import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.ConnectionTypeDefinitionConfigurer
import org.springframework.graphql.execution.GraphQlSource


@Configuration(proxyBeanMethods = false)
class GraphQlConfig {
    @Bean
    fun sourceBuilderCustomizer(): GraphQlSourceBuilderCustomizer {
        return GraphQlSourceBuilderCustomizer {
            GraphQlSource.schemaResourceBuilder()
                .schemaResources()
                .configureTypeDefinitions(ConnectionTypeDefinitionConfigurer())
        }
    }
}