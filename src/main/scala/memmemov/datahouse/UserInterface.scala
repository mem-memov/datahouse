package memmemov.datahouse

import cats.effect.IO
import cats.effect.std.{Dispatcher, Queue}
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import javafx.application.Platform
import memmemov.datahouse.speech.ButtonMessage

object UserInterface:

  def apply(
    dispatcher: Dispatcher[IO],
    recorderQueue: Queue[IO, Option[ButtonMessage]]
  ): JFXApp3 =
    val textInput = viewModel.TextInput("")
    val newUserInterface = view.Application(textInput, dispatcher, recorderQueue)
    newUserInterface
