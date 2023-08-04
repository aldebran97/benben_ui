package com.example.benben_front

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * 启动类
 * @author aldebran
 * @since 2023-08-04
 */
class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 1000.0, 600.0)
        stage.title = "犇犇🐂"
        stage.scene = scene
        stage.isFullScreen = true
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}