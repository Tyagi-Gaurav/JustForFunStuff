package com.jffs.app

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = [
    "com.jffs.app.resource",
    "com.jffs.app.config",
    "com.jffs.app"
])
@EnableAutoConfiguration(exclude = [MongoAutoConfiguration::class, MongoReactiveAutoConfiguration::class])
open class JffsApplication

fun main(args: Array<String>) {
    runApplication<JffsApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}