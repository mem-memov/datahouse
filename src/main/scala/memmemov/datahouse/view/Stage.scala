package memmemov.datahouse.view

import scalafx.Includes.*
import cats.effect.IO
import cats.effect.std.Dispatcher
import memmemov.datahouse.viewModel.TextInput
import scalafx.application.JFXApp3

object Stage:

  def apply(
    textInput: TextInput,
    dispatcher: Dispatcher[IO]
  ): JFXApp3.PrimaryStage =

    val newStage = new JFXApp3.PrimaryStage {
      title.value = "Datahouse"
      width = 600
      height = 450
    }

    val newScene = Scene(newStage.width, newStage.height, textInput, dispatcher)

    newStage.scene = newScene

    newStage

