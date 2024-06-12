package com.jffs.admin.app

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = [
    "com.jffs.admin.app.config",
    "com.jffs.admin.app",
    "com.jffs.admin.app.resource"
])
@EnableAutoConfiguration(exclude = [MongoAutoConfiguration::class, MongoReactiveAutoConfiguration::class])
open class JffsAdminApplication

fun main(args: Array<String>) {
    runApplication<JffsAdminApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}