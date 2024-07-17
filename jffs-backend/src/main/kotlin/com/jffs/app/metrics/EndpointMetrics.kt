package com.jffs.app.metrics

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.TimeUnit

class EndpointMetrics(private val meterRegistry: MeterRegistry) {
    fun createHistogramFor(metricName: String?, vararg tags: String?): Histogram {
        return Histogram(
            Timer.builder(metricName!!)
                .tags(*tags)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry)
        )
    }

    class Histogram(private val timer: Timer) {
        private var threadLocal: ThreadLocal<Instant>? = null

        fun start() {
            threadLocal = ThreadLocal.withInitial { Instant.now() }
        }

        fun observe() {
            val startTime = threadLocal!!.get()
            val duration = Instant.now().toEpochMilli() - startTime.toEpochMilli()
            LOG.debug("Duration of request for {} was : {} milli seconds", timer.id, duration)
            timer.record(duration, TimeUnit.MILLISECONDS)
            threadLocal!!.remove()
        }
    }

    companion object {
        private val LOG: Logger = LogManager.getLogger("APP")
    }
}
