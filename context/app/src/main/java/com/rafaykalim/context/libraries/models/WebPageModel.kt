package com.rafaykalim.context.libraries.models

import org.jsoup.nodes.Document

data class WebPageModel (
    var title : String,
    var url : String,
    var html : Document
)