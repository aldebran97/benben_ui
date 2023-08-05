package com.example.benben_front.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject

import com.example.benben_front.*
import com.example.benben_front.TTS_URL
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.concurrent.TimeUnit


/**
 * 接口服务
 * @author aldebran
 * @since 2023-08-04
 */
class WebService {

    companion object
}

fun WebService.Companion.post(url: String, reqBody: String): String {
    val client = OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(3600L, TimeUnit.SECONDS)
            .build()
    val mediaType = "application/json".toMediaType()
    val body = reqBody.toRequestBody(mediaType)
    val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build()
    val response = client.newCall(request).execute()
    return response.body.string()
}


// 非流式聊天
fun WebService.Companion.notStreamChat(history: List<List<String>>, prompt: String): String {
//    return HttpRequest.post(Constants.CHAT_SERVICE_NOT_STREAM_URL).timeout(1000 * 3600).body(
//            JSONObject().apply {
//                this["history"] = history
//                this["prompt"] = prompt
//            }.let { JSON.toJSONString(it) }
//    ).let { it.execute().body() }

    return post(Constants.CHAT_SERVICE_NOT_STREAM_URL,
            JSONObject().apply {
                this["history"] = history
                this["prompt"] = prompt
            }.let { JSON.toJSONString(it) })

}

// 流式聊天


// tts
fun WebService.Companion.tts(text: String, audioFile: File) {
    var st = System.currentTimeMillis()
//    HttpRequest.post(Constants.TTS_URL).timeout(1000 * 3600).body(
//            JSONObject().apply {
//                this["text"] = text
//                this["output_file"] = audioFile.absolutePath
//            }.let { JSON.toJSONString(it) }
//    ).let { it.execute().body() }
    post(Constants.TTS_URL,
            JSONObject().apply {
                this["text"] = text.replace("犇", "奔").replace("🐂", "")
                this["output_file"] = audioFile.absolutePath
            }.let { JSON.toJSONString(it) }
    )
    assert(audioFile.isFile)
    var ed = System.currentTimeMillis()
    println("tts, audio file: $audioFile, time usage: ${(ed - st) / 1000.0}s")
}

// aptv，假设文件名是唯一的
fun WebService.Companion.aptv(audioFile: File, videoFile: File) {
    if (audioFile.parentFile != File(Constants.TEMP_FOLDER)) {
        var audioFile2 = File(Constants.TEMP_FOLDER, audioFile.name)
        println("需拷贝 $audioFile -> $audioFile2")
        Files.copy(audioFile.toPath(), audioFile2.toPath(), StandardCopyOption.REPLACE_EXISTING)
    } else {
        println("不需要拷贝 $audioFile")
    }
    var st = System.currentTimeMillis()
//    HttpRequest.post(Constants.APTV_URL).timeout(1000 * 3600).body(
//            JSONObject().apply {
//                this["audio_path"] = "${Constants.DOCKER_FOLDER}/${audioFile.name}"
//                this["video_path"] = "${Constants.DOCKER_FOLDER}/${videoFile.name}"
//            }.let { JSON.toJSONString(it) }
//    ).let { it.execute().body() }
    post(Constants.APTV_URL,
            JSONObject().apply {
                this["audio_path"] = "${Constants.DOCKER_FOLDER}/${audioFile.name}"
                this["video_path"] = "${Constants.DOCKER_FOLDER}/${videoFile.name}"
            }.let { JSON.toJSONString(it) })
    if (videoFile.parentFile != File(Constants.TEMP_FOLDER)) {
        var videoFile2 = File(Constants.TEMP_FOLDER, audioFile.name)
        println("需拷贝 $videoFile2 -> $videoFile")
        Files.copy(videoFile2.toPath(), videoFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    } else {
        println("不需要拷贝 $videoFile")
    }
    assert(videoFile.isFile)
    var ed = System.currentTimeMillis()
    println("aptv, video file: $videoFile, time usage: ${(ed - st) / 1000.0}s")
}

fun WebService.Companion.similaritySearch(query: String,
                                          libNames: List<String>,
                                          topK: Int = 10,
                                          threshold: Double = 0.5): List<JSONObject> {
    return post(Constants.SIMILARITY_SEARCH_URL,
            JSONObject().apply {
                this["libNames"] = libNames
                this["topK"] = topK
                this["text"] = query
            }.let { JSON.toJSONString(it) }).let { JSON.parseArray(it) }.filter {
        it as JSONObject
        it.getDouble("score") >= threshold
    } as List<JSONObject>
}