package memmemov.datahouse

import cats.effect.IO
import cats.effect.std.Dispatcher
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import javafx.application.Platform

object UserInterface:

  def apply(
    dispatcher: Dispatcher[IO]
  ): JFXApp3 =
    val textInput = viewModel.TextInput("")
    val newUserInterface = view.Application(textInput, dispatcher)
    newUserInterface
