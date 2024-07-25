package com.jffs.app.metrics

import com.jffs.app.metrics.MetricConstants.ACTION_TAG
import com.jffs.app.metrics.MetricConstants.PAGE_TAG
import com.jffs.app.metrics.MetricConstants.REQUEST_COUNT
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class UIEventCounter(private val meterRegistry: MeterRegistry) {
    fun increment(action: String, page: String) {
        val requestCount = Counter.builder(REQUEST_COUNT)
            .tags(ACTION_TAG, action, PAGE_TAG, page)
            .register(meterRegistry)
        requestCount.increment()
    }
}
