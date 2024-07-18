package com.jffs.app.metrics

import com.jffs.app.metrics.MetricConstants.INFRA_REQUEST_COUNT
import com.jffs.app.metrics.MetricConstants.METHOD_TAG
import com.jffs.app.metrics.MetricConstants.PATH_TAG
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class EndpointRequestCounter(private val meterRegistry: MeterRegistry) {
    fun increment(method: String, path: String) {
        val requestCount = Counter.builder(INFRA_REQUEST_COUNT)
            .tags(METHOD_TAG, method, PATH_TAG, path)
            .register(meterRegistry)
        requestCount.increment()
    }
}
