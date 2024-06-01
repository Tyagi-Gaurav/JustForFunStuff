package com.jffs.app.config

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("database")
data class DatabaseConfig(val username: String,
                          val password: String,
                          val name: String,
                          val host: String,
                          val scheme : String) {
    fun connectionString(): String {
        return "$scheme://$username:$password@$host/?retryWrites=true&appName=$name"
    }
}


