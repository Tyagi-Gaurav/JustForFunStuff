package com.jffs.app.interceptor

import com.jffs.app.metrics.EndpointMetrics
import com.jffs.app.metrics.EndpointRequestCounter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

class MetricsInterceptor(private val endpointRequestCounter: EndpointRequestCounter,
                         private val endpointMetrics: EndpointMetrics) : HandlerInterceptor {
    val histogram = endpointMetrics.createHistogramFor("response.latency")

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        histogram.start()
        endpointRequestCounter.increment(request.method, request.pathInfo)
        return super.preHandle(request, response, handler)
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        super.postHandle(request, response, handler, modelAndView)
        histogram.observe()
    }
}