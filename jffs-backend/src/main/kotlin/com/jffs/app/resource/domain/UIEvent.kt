package com.jffs.app.resource.domain

import kotlinx.serialization.Serializable

@Serializable
data class UIEvent(val action: String, val page: String)
