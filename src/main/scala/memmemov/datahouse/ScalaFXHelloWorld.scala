package memmemov.datahouse

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene

object ScalaFXHelloWorld extends JFXApp3 {

  override def start(): Unit = {

    val textInput = viewModel.TextInput("")

    stage = new JFXApp3.PrimaryStage {
      title.value = "Datahouse"
      width = 600
      height = 450
      scene = new Scene { self =>
        content = view.Layout(self, textInput)
      }
    }
  }
}
