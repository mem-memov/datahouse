package memmemov.datahouse.view

import scalafx.Includes.*
import cats.effect.IO
import cats.effect.std.{Dispatcher, Queue}
import memmemov.datahouse.speech.ButtonMessage
import memmemov.datahouse.view
import memmemov.datahouse.viewModel.TextInput
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.Scene

object Scene:

  def apply(
    containerWidth: ReadOnlyDoubleProperty,
    containerHeight: ReadOnlyDoubleProperty,
    textInput: TextInput,
    dispatcher: Dispatcher[IO],
    recorderQueue: Queue[IO, Option[ButtonMessage]]
  ): Scene =

    val newScene = new Scene {}

    val newLayout = Layout(newScene.width, newScene.height, textInput, dispatcher, recorderQueue)

    newScene.content = newLayout

    newScene