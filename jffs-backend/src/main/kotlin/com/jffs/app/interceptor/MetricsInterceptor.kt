package com.jffs.app.interceptor

import com.jffs.app.metrics.EndpointLatencyTimer
import com.jffs.app.metrics.EndpointRequestCounter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

class MetricsInterceptor(private val endpointRequestCounter: EndpointRequestCounter,
                         private val endpointMetrics: EndpointLatencyTimer) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var pathInfo = request.pathInfo
        pathInfo?.let {
            endpointRequestCounter.increment(request.method, pathInfo)
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
        request.pathInfo?.let {
            var startTime = java.lang.Long.parseLong(request.getAttribute("X-Request-StartTime").toString())
            endpointMetrics.observe(startTime, request.method, request.pathInfo)
        }
    }
}