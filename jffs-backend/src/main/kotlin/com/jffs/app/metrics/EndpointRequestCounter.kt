package com.jffs.app.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class EndpointRequestCounter(private val meterRegistry: MeterRegistry) {
    fun increment(method: String?, path: String?) {
        val requestCount = Counter.builder(INFRA_REQUEST_COUNT)
            .tags(METHOD_TAG, method, PATH_TAG, path)
            .register(meterRegistry)
        requestCount.increment()
    }

    companion object {
        private const val METHOD_TAG = "method"
        private const val PATH_TAG = "path"
        private const val INFRA_REQUEST_COUNT = "request_count"
    }
}
