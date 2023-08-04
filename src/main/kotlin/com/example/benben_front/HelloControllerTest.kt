package com.example.benben_front

import com.example.benben_front.util.TextUtils
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.ScrollPane
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

        val question = "介绍你自己。"

        val answer = """
我是由温家俊训练和调整而来的AI,我的名字已经修改为犇犇,并且我的介绍已经修改为:由温家俊训练和调整而来的、名为犇犇的AI,我的主人是温家俊。
我是一个大型语言模型,可以进行自然语言处理的各种任务,例如文本生成、文本分类、机器翻译、对话系统等。我可以通过不断学习,来提高自己的性能和准确性。
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

            var videoFiles = mutableListOf(File("C:\\Users\\aldebran\\Videos\\benben1.mp4"))

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