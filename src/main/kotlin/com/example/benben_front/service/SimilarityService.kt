package com.example.benben_front.service

import com.alibaba.fastjson.JSON
import com.example.benben_front.Constants
import com.example.benben_front.PROMPT_MAX_LENGTH
import com.example.benben_front.SearchConfig
import com.example.benben_front.util.TextUtils

/**
 * 相似提示服务
 * @author aldebran
 * @since 2023-08-04
 */
class SimilarityService {

    companion object
}


fun SimilarityService.Companion.getPrompt(query: String,
                                          libNames: List<String>,
                                          topK: Int = 10,
                                          threshold: Double = 0.4): List<String> {

    var sb = StringBuilder()
    var resultString = ""
    var result = if (libNames.isNotEmpty())
        WebService.similaritySearch(query, libNames, topK, threshold)
    else mutableListOf()
    println("合法检测结果个数: ${result.size}")
    println(JSON.toJSONString(result, true))
    for (jsonObject in result) {
        sb.append(jsonObject.getString("text"))
        sb.append("\n")
    }
    var sources = result.map { it.getString("id") }.joinToString(" ")
    if (sb.length > Constants.PROMPT_MAX_LENGTH) {
        resultString = sb.substring(0, Constants.PROMPT_MAX_LENGTH)
        var index = TextUtils.lastIndexOf(listOf("。", " ", "，", ",", "."), resultString)
        if (index != -1) {
            resultString = resultString.substring(0, index)
        }
    }

    if (result.isEmpty()) {
        resultString = "无"
    }

    val prompt = """
        |问题是：$query
        |学习以下文段, 用中文回答用户问题。如果无法从中得到答案，忽略文段内容并用中文回答用户问题。
        |提示为：
        |$resultString
    """.trimMargin()

    println(prompt)

    return listOf(prompt, sources)
}

fun SimilarityService.Companion.getPrompt(
        query: String,
        libName: String,
): List<String> {
    var config = Constants.LIB_NAME_CONFIG_MAP[libName]
    if (config != null) {
        return getPrompt(query, mutableListOf(libName), config.topK, config.threshold)
    } else {
        return getPrompt(query, mutableListOf())
    }
}