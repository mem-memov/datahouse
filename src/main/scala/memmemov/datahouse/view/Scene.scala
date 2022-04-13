package memmemov.datahouse.view

import scalafx.Includes.*
import cats.effect.IO
import cats.effect.std.Dispatcher
import memmemov.datahouse.view
import memmemov.datahouse.viewModel.TextInput
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.Scene

object Scene:

  def apply(
    containerWidth: ReadOnlyDoubleProperty,
    containerHeight: ReadOnlyDoubleProperty,
    textInput: TextInput,
    dispatcher: Dispatcher[IO]
  ): Scene =

    val newScene = new Scene {}

    val newLayout = Layout(newScene.width, newScene.height, textInput, dispatcher)

    newScene.content = newLayout

    newScene