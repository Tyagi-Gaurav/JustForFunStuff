package com.jffs.app.config

import com.jffs.app.interceptor.MetricsInterceptor
import com.jffs.app.metrics.EndpointLatencyTimer
import com.jffs.app.metrics.EndpointRequestCounter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class InterceptorConfig(
    @Autowired val endpointRequestCounter: EndpointRequestCounter,
    @Autowired val endpointMetrics: EndpointLatencyTimer
    ) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry)
        registry.addInterceptor(MetricsInterceptor(endpointRequestCounter, endpointMetrics))
    }
}