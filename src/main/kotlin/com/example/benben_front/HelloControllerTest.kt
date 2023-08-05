package com.example.benben_front

import com.example.benben_front.util.TextUtils
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextArea
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.web.WebView
import javafx.util.Duration
import java.io.File
import java.util.Date

/**
 * Test Controller
 * @author aldebran
 * @since 2023-08-04
 */
class HelloControllerTest {

    @FXML
    private lateinit var chatContentWV: WebView

    @FXML
    private lateinit var scrollPane: ScrollPane

    private val testHistory = mutableListOf(mutableListOf("", ""))

    @Volatile
    private var historyLastUpdateTime = Date()

    @Volatile
    private var historyLastCheckTime = historyLastUpdateTime

    private var timeLine: Timeline? = null

    @Volatile
    private var stopStatus = false

    @FXML
    private fun clickClear() {

    }

    @FXML
    private fun clickSubmit() {
        println("clickSubmit start")
        println(this.javaClass.getResource("all_html.css").toExternalForm())
        chatContentWV.engine.userStyleSheetLocation = this.javaClass.getResource("all_html.css").toExternalForm()

        val question = "介绍木星的卫星：木卫二"

        val answer = """
以下是关于木卫二的信息：

木卫二是一颗位于木星身边的卫星，距离木星平均距离为670,900公里，公转一周只需三天半的时间。它的轨道十分接近正圆，偏心率仅0.009。与其他的伽利略卫星一样，木卫二也被潮汐锁定，因此有一个半球永远朝向木星。

由木星和其他卫星不同方向的重力牵引所转化成的热和能量为有可能发生的，冰层内部液化成海洋，以及驱动表层下的地质运动提供了必要的条件。

木卫二被称为“欧罗巴”，是木星卫星中第四大的直径和质量，也是公转轨道距离木星第六近的一颗。在已知的79颗木星卫星中，木卫二是直径和质量第四大的，公转轨道距离木星第六近的一颗。

木卫二主要由硅酸盐岩石构成，并具有水- 冰地壳，可能是一个铁- 镍核心；有稀薄的大气层，主要由氧气组成；表面有大量裂缝和条纹，而陨石坑比较罕见，有在太阳系任何已知的固体物体的最光滑表面。
""".trimIndent()

        startTimeLine()

        Thread {
            // simulate stream response
            testHistory[0][0] = question
            answer.forEach { c ->
                testHistory[0][1] += c.toString()
                Thread.sleep(100)
                historyLastUpdateTime = Date()
            }
            stopStatus = true

        }.start()

        Platform.runLater {

            println("runLater start")

            var videoFiles = mutableListOf(File("D:\\user_dir\\temp\\benben\\1691194020464.mp4"))

            var i = 0

            var mediaView: MediaView


            fun play() {
                var media = Media(videoFiles[i].toURI().toString())
                var mediaPlayer = MediaPlayer(media)

                mediaPlayer.setOnReady {
                    println("ready")
                    mediaPlayer.play()
                }
                mediaPlayer.setOnEndOfMedia {
                    println("end")
//                mediaPlayer.dispose()
                    i = (i + 1) % videoFiles.size
                    if (i != 0) {
                        play()
                    }

                }
                mediaView = MediaView(mediaPlayer)
                scrollPane.content = mediaView
            }

            play()

            println("runLater end")
        }

        println("clickSubmit end")

    }

    @FXML
    fun initialize() {

    }

    fun startTimeLine() {

        historyLastUpdateTime = Date()
        historyLastCheckTime = historyLastUpdateTime
        stopStatus = false

        var keyFrame = KeyFrame(Duration.millis(400.0), { event ->
//            println(historyLastUpdateTime.after(historyLastCheckTime))
            var exit = stopStatus

            if (historyLastUpdateTime.after(historyLastCheckTime)) {
                chatContentWV.engine.loadContent(TextUtils.historyToHtml(testHistory))
//                chatContentWV.text = TextUtils.historyToHtml(testHistory)
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