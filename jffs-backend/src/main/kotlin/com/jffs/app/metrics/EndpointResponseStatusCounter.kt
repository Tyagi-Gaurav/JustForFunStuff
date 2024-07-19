package com.jffs.app.metrics

import com.jffs.app.metrics.MetricConstants.RESPONSE_STATUS
import com.jffs.app.metrics.MetricConstants.STATUS_TAG
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry

class EndpointResponseStatusCounter(private val meterRegistry: MeterRegistry) {

    fun increment(status: Int) {
        val responseStatus = Counter.builder(RESPONSE_STATUS)
            .tags(STATUS_TAG, status.toString())
            .register(meterRegistry)
        responseStatus.increment()
    }
}
