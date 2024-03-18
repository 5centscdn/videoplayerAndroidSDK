package com.fivecentscdnanalytics.data

import org.json.JSONArray

class FiveCentsData {
    var hashID: String? = null
    var URL: String? = null
    var title: String? = null
    val tags = mutableListOf<String>()
    var showCV: Boolean? = null
    var viewerID: String? = null
    var viewID: String? = null
    var userAgent:  String? = null
    var reqType: String? = null
    var referrer: String? = null
    var valueOne: Int? = 0
    var valueTwo: Int? = 0

    fun retrieveTags(): JSONArray {
        val tagsArray = JSONArray()
        for (tag in tags) {
            tagsArray.put(tag)
        }
        return tagsArray
    }

    fun setTags(vararg tags: String) {
        this.tags.clear()
        this.tags.addAll(tags)
    }


}