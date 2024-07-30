package com.jffs.app.interceptor

import com.jffs.app.metrics.EndpointLatencyTimer
import com.jffs.app.metrics.EndpointRequestCounter
import com.jffs.app.metrics.EndpointResponseStatusCounter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

class MetricsInterceptor(
    private val endpointRequestCounter: EndpointRequestCounter,
    private val endpointLatency: EndpointLatencyTimer,
    private val endpointStatusMetric: EndpointResponseStatusCounter
) : HandlerInterceptor {
    private val ACCESS_LOG: Logger = LogManager.getLogger("ACCESS")
    private val X_REQUEST_START_TIME = "X-Request-StartTime"

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var requestURI = request.requestURI
        if (request.getAttribute(X_REQUEST_START_TIME) == null) {
            request.setAttribute(X_REQUEST_START_TIME, System.currentTimeMillis())
            ACCESS_LOG.info("Inside prehandle for ${request.method}, $requestURI")
            requestURI?.let {
                endpointRequestCounter.increment(request.method, requestURI)
            }
        }
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
            val startTime = java.lang.Long.parseLong(request.getAttribute(X_REQUEST_START_TIME).toString())
            endpointLatency.observe(startTime, request.method, request.requestURI)
        }
        endpointStatusMetric.increment(response.status)
    }
}