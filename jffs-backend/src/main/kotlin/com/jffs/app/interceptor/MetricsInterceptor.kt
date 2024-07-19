package com.jffs.app.interceptor

import com.jffs.app.metrics.EndpointLatencyTimer
import com.jffs.app.metrics.EndpointRequestCounter
import com.jffs.app.metrics.EndpointResponseStatusCounter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

class MetricsInterceptor(
    private val endpointRequestCounter: EndpointRequestCounter,
    private val endpointLatency: EndpointLatencyTimer,
    private val endpointStatusMetric: EndpointResponseStatusCounter
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var requestURI = request.requestURI
        requestURI?.let {
            endpointRequestCounter.increment(request.method, requestURI)
        }
        request.setAttribute("X-Request-StartTime", System.currentTimeMillis())
        return super.preHandle(request, response, handler)
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        super.postHandle(request, response, handler, modelAndView)
        request.requestURI?.let {
            val startTime = java.lang.Long.parseLong(request.getAttribute("X-Request-StartTime").toString())
            endpointLatency.observe(startTime, request.method, request.requestURI)
        }
        endpointStatusMetric.increment(response.status)
    }
}