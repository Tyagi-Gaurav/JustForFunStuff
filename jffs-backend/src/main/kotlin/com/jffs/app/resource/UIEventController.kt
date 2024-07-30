package com.jffs.app.resource

import com.jffs.app.metrics.UIEventCounter
import com.jffs.app.resource.domain.UIEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class UIEventController(@Autowired val uiEventCounter: UIEventCounter) {
    @PostMapping("/v1/ui/event", produces = ["application/json"])
    fun record(@RequestBody event: UIEvent): ResponseEntity<String> {
        uiEventCounter.increment(event)
        return ResponseEntity.ok().build();
    }
}