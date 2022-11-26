package com.example._2

import javafx.animation.FadeTransition
import javafx.animation.TranslateTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
//import javafx.scene.layout.HBox
//import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.media.MediaPlayer
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import javafx.stage.Screen
import java.nio.file.Paths

enum class ImageStyle {
    CIRCLE, RECTANGLE
}

class Config {
    var alpha = 0.9
    var openTime = 10000.0
    var imageType = ImageStyle.CIRCLE // CIRCLE / RECTANGLE
    var imageAli = "file:storage/ImageAli.png"
    var imageLike = "file:storage/ImageLike.png"
    var soundPath = "storage/Sale.mp3"
    var PosX = "Right" //  Left / Right
    var PosY = "Bottom"  //  Bottom / Top
    var title = "Во-оля"
    var titleStyle = "-fx-text-fill: Indigo; -fx-font-size: 18px; -fx-padding: -10 0 0 50"
    var message = "Buy the Soap"
    var messageStyle = "-fx-text-fill: red; -fx-font-size: 16px; -fx-padding: 50 0 0 45"
    var appName = "AliExpression"
    var appNameStyle = "-fx-text-fill: DarkSlateGray; -fx-font-size: 12px; -fx-padding: -5 0 0 100"
    var msg = "Введите текст"
    var msgStyle = "-fx-text-fill: Maroon; -fx-font-size: 14px; -fx-padding: -30 0 0 32"
    var reportStyle = "-fx-text-fill: BlueViolet; -fx-font-size: 13px; -fx-padding: 0 0 5 50"
    var btn1 = "True"  // True / False
    var btn1Style = "-fx-background-color: LimeGreen; -fx-text-fill: Yellow; -fx-font-size: 17px"
    var btn2 = "True"  // True / False
    var btn2Style = "-fx-background-color: FireBrick; -fx-text-fill: Black; -fx-font-size: 17px;"
    var cursor = "HAND"
    var button_event = "music_stop"  //music / text
    var TransitionAnimType = "Translate" // Translate / Fade
    var rootStyle = "-fx-background-color: #ffffff; -fx-padding: 10 10 10 10"
    var textField = "On" // On / Off
}

class Toast {
    private var config = Config()
    private val windows = Stage()
    private var root = BorderPane()
    private val grid = GridPane()
    private var primaryScreenBounds = Screen.getPrimary().getVisualBounds()
    private val iconBorder = if (config.imageType == ImageStyle.RECTANGLE) {
        Rectangle(100.0, 100.0)   //width,  height
    }
    else {
        Circle(60.0, 60.0, 60.0)
    }
    private val path = Media(Paths.get(config.soundPath).toUri().toString())
    val mediaPlayer = MediaPlayer(path)

    class Builder {
        private var config = Config()

        fun build(): Toast  {
            val toast = Toast()
            toast.config = config
            toast.build()

            return toast
        }
    }

    private fun build() {
        windows.initStyle(StageStyle.TRANSPARENT)

        windows.scene = Scene(root)
        windows.scene.fill = Color.TRANSPARENT
        windows.sizeToScene()

        root.style = config.rootStyle

        setImage()


        val text = TextField()
        val title = Label(config.title)
        val message = Label(config.message)
        val appName = Label(config.appName)
        val msg = Label(config.msg)
        msg.style = config.msgStyle
        title.style = config.titleStyle
        message.style = config.messageStyle
        appName.style = config.appNameStyle

        grid.setHgap(1.0)
        grid.setVgap(2.0)
        grid.add(title, 2, 0, 1, 1)
        grid.add(message, 2, 0, 1, 1)
        grid.add(appName, 2, 1, 1, 1)
        if (config.textField == "On") {
            grid.add(msg, 2, 3, 1, 1)
            grid.add(text, 2, 4, 1, 1)
            text.setOnAction {
                text.isVisible = false
                val report = Label(text.getText())
                report.style = config.reportStyle
                grid.add(report, 2, 4)
            }
        }

        if (config.btn1 == "True") {
            val button1 = Button("Accept")
            button1.style = config.btn1Style
            button1.cursor = Cursor.cursor(config.cursor)
            button1.addEventHandler(MouseEvent.MOUSE_CLICKED) {
                if (config.button_event == "text"){
                    message.text = "Successfully"
                    iconBorder.setFill(ImagePattern(Image(config.imageLike)))
                    closeAnimation()
                    mediaPlayer.stop()
                }
                else if(config.button_event == "music_stop") {
                    message.text = "more_load"
                    mediaPlayer.volume = (mediaPlayer.volume + 0.1)
                }
            }
            grid.add(button1, 1, 5, 1, 1)
        }
        if (config.btn2 == "True") {
            val button2 = Button("Close")
            button2.style = config.btn2Style
            button2.cursor = Cursor.cursor(config.cursor)

            button2.addEventHandler(MouseEvent.MOUSE_CLICKED) {
                closeAnimation()
                mediaPlayer.stop()
            }
            grid.add(button2, 2, 5, 1, 1)
        }

//        hbox.setSpacing(150.0)
//        hbox.padding = Insets(10.0, 0.0, 0.0, 0.0)
//        vbox.setSpacing(2.5)
//        box.children.add(vbox)
//        box.children.add(hbox)
//        root.center = box
//        root.bottom = hbox
        root.center = grid
    }

    private fun setImage() {
        if (config.imageAli.isEmpty()) {
            return
        }

        iconBorder.setFill(ImagePattern(Image(config.imageAli)))
        grid.add(iconBorder, 0, 0, 2, 3)
    }

    private fun openAnimation() {
        if (config.PosX == "Left") {
            windows.x = 0.0
        }
        else if (config.PosX == "Right") {
            windows.x = primaryScreenBounds.getWidth() - windows.scene.width
        }

        if (config.PosY == "Top") {
            windows.y = 0.0
        }
        else if (config.PosY == "Bottom") {
            windows.y = primaryScreenBounds.getHeight() - windows.scene.height
        }

        if (config.TransitionAnimType == "Translate") {
            val anim = TranslateTransition(Duration.millis(1500.0), root)

            if (config.PosY == "Bottom" && config.PosX == "Right") {
                anim.setFromY(windows.scene.height)
                anim.setByY(-windows.scene.height)
            } else if (config.PosY == "Bottom" && config.PosX == "Left") {
                anim.setFromX(-windows.scene.width)
                anim.setByX(windows.scene.width)
            } else if (config.PosY == "Top" && config.PosX == "Left") {
                anim.setFromY(-windows.scene.height)
                anim.setByY(windows.scene.height)
            } else if (config.PosY == "Top" && config.PosX == "Right") {
                anim.setFromX(windows.scene.width)
                anim.setByX(-windows.scene.width)
            }
            anim.cycleCount = 1
            anim.play()
        }
        else if(config.TransitionAnimType == "Fade") {
            val anim = FadeTransition(Duration.millis(1500.0), root)
            anim.fromValue = 0.0
            anim.toValue = config.alpha
            anim.cycleCount = 1
            anim.play()
        }
    }

    private fun closeAnimation() {
        if (config.TransitionAnimType == "Translate") {
            val anim = TranslateTransition(Duration.millis(1500.0), root)


            if (config.PosY == "Bottom" && config.PosX == "Right") {
                anim.setByY(windows.scene.height)
            } else if (config.PosY == "Bottom" && config.PosX == "Left") {
                anim.setByX(-windows.scene.width)
            } else if (config.PosY == "Top" && config.PosX == "Left") {
                anim.setByY(-windows.scene.height)
            } else if (config.PosY == "Top" && config.PosX == "Right") {
                anim.setByX(windows.scene.width)
            }

            anim.cycleCount = 1
            anim.onFinished = EventHandler {
                Platform.exit()
                System.exit(0)
            }
            anim.play()
        }
        else if (config.TransitionAnimType == "Fade") {
            val anim = FadeTransition(Duration.millis(1500.0), root)
            anim.fromValue = config.alpha
            anim.toValue = 0.0
            anim.cycleCount = 1
            anim.onFinished = EventHandler {
                Platform.exit()
                System.exit(0)
            }
            anim.play()
        }
    }

    private fun music() {
        if(config.soundPath.isEmpty()){
            return
        }
        mediaPlayer.play()
        mediaPlayer.setStopTime(Duration(7500.0))
        mediaPlayer.setVolume(0.15)
    }

    fun start() {
        windows.show()
        openAnimation()
        music()
        val thread = Thread {
            try {
                Thread.sleep(config.openTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            closeAnimation()
        }
        Thread(thread).start()
    }
}

class SomeClass: Application() {
    override fun start(p0: Stage?) {
        val toast = Toast.Builder().build()
        toast.start()
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SomeClass::class.java)
        }
    }
}