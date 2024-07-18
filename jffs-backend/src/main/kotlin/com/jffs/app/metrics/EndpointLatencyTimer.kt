package com.jffs.app.metrics

import com.jffs.app.metrics.MetricConstants.METHOD_TAG
import com.jffs.app.metrics.MetricConstants.PATH_TAG
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import java.util.concurrent.TimeUnit

class EndpointLatencyTimer(private val meterRegistry: MeterRegistry) {

    fun observe(startTime: Long, methodTag: String, pathTag: String) {
        val timer = Timer.builder("request.latency")
            .tags(METHOD_TAG, methodTag, PATH_TAG, pathTag)
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry)

        val duration = System.currentTimeMillis() - startTime
        timer.record(duration, TimeUnit.MILLISECONDS)
    }
}
