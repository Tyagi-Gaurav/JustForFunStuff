package com.jffs.app.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

class LoggingInterceptor : HandlerInterceptor {
    private val ACCESS_LOG: Logger = LogManager.getLogger("ACCESS")

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        return super.preHandle(request, response, handler)
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        ACCESS_LOG.info("Request: ${request.getMethod()} ${request.requestURI}, headers: ${headerMapFrom(request)}")
        ACCESS_LOG.info("Response: ${response.status} ${request.getMethod()} ${request.requestURI}, headers: ${headerMapFrom(
            response)}")
    }

    private fun headerMapFrom(response: HttpServletResponse): Map<String, String> {
        return response.headerNames
            .toList()
            .map { it to response.getHeader(it)}.toMap()
    }

    private fun headerMapFrom(request: HttpServletRequest): Map<String, String> {
        return request.headerNames
            .toList()
            .map { it to request.getHeader(it)}.toMap()
    }
}