package com.example.benben_front

import com.alibaba.fastjson.JSON
import com.example.benben_front.service.*

import org.junit.jupiter.api.Test
import java.io.File


/**
 * web service test
 * @author aldebran
 * @since 2023-08-04
 */
class WebServiceTest {

    @Test
    fun notStreamChat() {
        println(WebService.notStreamChat(mutableListOf(), "你好"))
    }

    @Test
    fun tts() {
        WebService.tts("你好，欢迎来到这个世界！", File("D:\\user_dir\\temp\\benben\\test.wav"))
    }

    @Test
    fun aptv() {
        WebService.aptv(File("D:\\user_dir\\temp\\benben\\test.wav"),
                File("D:\\user_dir\\temp\\benben\\test.mp4"))
    }

    @Test
    fun similaritySearch() {
        WebService.similaritySearch("介绍木卫二",
                mutableListOf("wiki_interesting_lib"),
                5, 0.4).let {
            println(JSON.toJSONString(it, true))
        }
    }
}
