package com.jffs.app.metrics

import com.jffs.app.metrics.MetricConstants.ACTION_TAG
import com.jffs.app.metrics.MetricConstants.COMPONENT_TAG
import com.jffs.app.metrics.MetricConstants.PAGE_TAG
import com.jffs.app.metrics.MetricConstants.UI_EVENT
import com.jffs.app.resource.domain.UIEvent
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class UIEventCounter(private val meterRegistry: MeterRegistry) {
    fun increment(uiEvent: UIEvent) {
        val requestCount = Counter.builder(UI_EVENT)
            .tags(PAGE_TAG, uiEvent.page, COMPONENT_TAG, uiEvent.component, ACTION_TAG, uiEvent.action)
            .register(meterRegistry)
        requestCount.increment()
    }
}
