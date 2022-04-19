package memmemov.datahouse.view

import scalafx.Includes.*
import cats.effect.IO
import cats.effect.std.{Dispatcher, Queue}
import memmemov.datahouse.configuration.StorageDirectory
import memmemov.datahouse.speech.ButtonMessage
import memmemov.datahouse.viewModel.TextInput
import scalafx.application.JFXApp3
import scalafx.scene.Scene

class Application(
 textInput: TextInput,
 dispatcher: Dispatcher[IO],
 recorderQueue: Queue[IO, Option[ButtonMessage]],
 storageDirectory: StorageDirectory
) extends JFXApp3: // extending is needed for launching the Platform

  override def start(): Unit =
    stage = Stage(textInput, dispatcher, recorderQueue, storageDirectory)


object Application:

  def apply(
    textInput: TextInput,
    dispatcher: Dispatcher[IO],
    recorderQueue: Queue[IO, Option[ButtonMessage]],
    storageDirectory: StorageDirectory
  ): Application =

    new Application(textInput, dispatcher, recorderQueue, storageDirectory)