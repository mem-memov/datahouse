package memmemov.datahouse.view

import scalafx.Includes.*
import cats.effect.IO
import cats.effect.std.Dispatcher
import memmemov.datahouse.viewModel.TextInput
import scalafx.application.JFXApp3
import scalafx.scene.Scene

class Application(
 textInput: TextInput,
 dispatcher: Dispatcher[IO]
) extends JFXApp3: // extending is needed for launching the Platform

  override def start(): Unit =
    stage = Stage(textInput, dispatcher)


object Application:

  def apply(
    textInput: TextInput,
    dispatcher: Dispatcher[IO]
  ): Application =

    new Application(textInput, dispatcher)