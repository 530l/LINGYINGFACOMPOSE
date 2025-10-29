package com.lyf.lingyingfacompose.test

import com.google.common.collect.Multimaps.index

val cache = mutableMapOf<String, MutableList<String>>()

fun t1(){
    val list = cache.getOrPut("users") { ArrayList() }
    list.add("Alice")
}