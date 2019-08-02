package com.rafaykalim.context.libraries

class KotlinUtils {
    fun genDirMsg(from : String, to : String, mode: String): String {
        return "${from} ???" +
                "${to} ???" +
                "${mode}"
    }
}