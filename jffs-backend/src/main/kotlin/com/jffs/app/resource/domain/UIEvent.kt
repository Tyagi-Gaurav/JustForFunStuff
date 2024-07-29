package com.jffs.app.resource.domain

import kotlinx.serialization.Serializable

@Serializable
data class UIEvent(val page: String,
                   val component: String,
                   val action: String)
