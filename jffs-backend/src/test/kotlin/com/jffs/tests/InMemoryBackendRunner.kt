package com.jffs.tests

import com.jffs.app.resource.domain.MeaningDTO
import com.jffs.app.resource.domain.WordDTO
import com.jffs.app.resource.domain.WordsDTO
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
@EnableAutoConfiguration(exclude = [MongoAutoConfiguration::class, MongoReactiveAutoConfiguration::class])
@ComponentScan("com.jffs.tests")
open class InMemoryBackendRunner

fun main(args: Array<String>) {
    runApplication<InMemoryBackendRunner>(*args)
}

@Controller
class HtmlController {

    @GetMapping("/v1/words")
    fun words(): ResponseEntity<WordsDTO> {
        return ResponseEntity.ok(WordsDTO(
            listOf(
                WordDTO(
                    "A Word",
                    listOf(MeaningDTO("A definition", listOf("synonym1", "synonym2"), listOf("example1", "example2")))
                )
            )
        ))
    }

}
