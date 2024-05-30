package com.jffs.app.config

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("database")
data class DatabaseConfig(val username: String,
                          val password: String,
                          val name: String,
                          val host: String,
                          val scheme : String) {
    fun connectionString(): String {
//        val connectionString =
//            "mongodb+srv://jffs-prod-user:dDsv13DNGbeomU8i@prod.0nv9qyd.mongodb.net/?retryWrites=true&w=majority&appName=Prod"
        return "$scheme://$username:$password@$host/?retryWrites=true&appName=$name"
    }
}


