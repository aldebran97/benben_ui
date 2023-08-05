package com.example.benben_front

import com.example.benben_front.service.*
import com.example.benben_front.util.TextUtils
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.TouchPoint
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.web.WebView
import javafx.util.Duration
import java.io.File
import java.lang.Exception
import java.util.*

/**
 * actual controller
 * @author aldebran
 * @since 2023-08-04
 */
class HelloControllerActual {

    @FXML
    private lateinit var chatContentWV: WebView

    @FXML
    private lateinit var scrollPane: ScrollPane

    @FXML
    private lateinit var inputTF: TextField

    @FXML
    private lateinit var knowledgeCB: ComboBox<String>

    private var history = mutableListOf<MutableList<String>>()

    @Volatile
    private var historyLastUpdateTime = Date()

    @Volatile
    private var historyLastCheckTime = historyLastUpdateTime

    private var timeLine: Timeline? = null

    @Volatile
    private var stopStatus = true

    @FXML
    private fun clickClear() {
        timeLine = null
        stopStatus = true
        historyLastUpdateTime = Date()
        historyLastCheckTime = historyLastUpdateTime
        history = mutableListOf()
        inputTF.text = ""
        chatContentWV.engine.loadContent("")
    }

    @FXML
    private fun clickSubmit() {
        println("clickSubmit start")
        var id = System.currentTimeMillis()
        var audioFile = File(Constants.TEMP_FOLDER, "$id.wav")
        var videoFile = File(Constants.TEMP_FOLDER, "$id.mp4")
        println(this.javaClass.getResource("all_html.css").toExternalForm())
        chatContentWV.engine.userStyleSheetLocation = this.javaClass.getResource("all_html.css").toExternalForm()

        // 分要点介绍木星的卫星：木卫二

        var question = inputTF.text
        var libName = knowledgeCB.value

        if (libName == null || libName == "无") libName = ""

        println("question: $question, libName: $libName")

        var prompt = ""
        var sources = ""

        SimilarityService.getPrompt(question, libName).let {
            prompt = it[0]
            sources = it[1]
        }

        startTimeLine()

        // update text view
        Thread {
            // 采用非流式，主要原因是视频生成比较慢
            var resposne = WebService.notStreamChat(history, prompt)
            println("response: $resposne")
            WebService.tts(resposne, audioFile)
            WebService.aptv(audioFile, videoFile)

            // update video view
            Platform.runLater {

                println("runLater start")

                var media = Media(videoFile.toURI().toString())
                var mediaPlayer = MediaPlayer(media)

                mediaPlayer.setOnReady {
                    println("ready")
                    mediaPlayer.play()
                }
                mediaPlayer.setOnEndOfMedia {
                    println("end")
//                mediaPlayer.dispose()
                }
                var mediaView = MediaView(mediaPlayer)
                scrollPane.content = mediaView

                println("runLater end")
            }

            // simulate stream response
            history.add(mutableListOf("", ""))
            history.last()[0] = question
            resposne.forEach { c ->
                history.last()[1] += c.toString()
                Thread.sleep(100)
                historyLastUpdateTime = Date()
            }
            history.last()[1] += """<br/> 来源: $sources"""
            historyLastUpdateTime = Date()
            stopStatus = true


        }.start()


        println("clickSubmit end")

    }

    @FXML
    fun initialize() {
        scrollPane.vvalue = 1.0
    }

    fun startTimeLine() {

        historyLastUpdateTime = Date()
        historyLastCheckTime = historyLastUpdateTime
        stopStatus = false

        var keyFrame = KeyFrame(Duration.millis(400.0), { event ->
//            println(historyLastUpdateTime.after(historyLastCheckTime))
            var exit = stopStatus

            if (historyLastUpdateTime.after(historyLastCheckTime)) {
                chatContentWV.engine.loadContent(TextUtils.historyToHtml(history))
                chatContentWV.engine.executeScript("window.scrollTo(0, 2000);")
                historyLastCheckTime = Date()
                event.consume()
            }

            if (exit) {
                timeLine!!.stop()
                println("stop timeLine")
            }

        })

        timeLine = Timeline(keyFrame).also {
            it.cycleCount = Animation.INDEFINITE
        }

        Thread {
            timeLine!!.play()
        }.start()

    }
}