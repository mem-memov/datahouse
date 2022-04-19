package memmemov.datahouse.view

import cats.effect.IO
import cats.effect.std.{Dispatcher, Queue}
import memmemov.datahouse.configuration.StorageDirectory
import memmemov.datahouse.speech.ButtonMessage
import scalafx.Includes.*
import memmemov.datahouse.viewModel.TextInput
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.Scene
import scalafx.scene.layout.VBox

object Layout:

  def apply(
    containerWidth: ReadOnlyDoubleProperty,
    containerHeight: ReadOnlyDoubleProperty,
    textInput: TextInput,
    dispatcher: Dispatcher[IO],
    recorderQueue: Queue[IO, Option[ButtonMessage]],
    storageDirectory: StorageDirectory
  ): VBox =

    val newLayout = new VBox:
      vb =>
        minWidth <== containerWidth
        minHeight <== containerHeight
        maxWidth <== minWidth
        maxHeight <== minHeight


    val newToolBar = ToolBar(newLayout.width, textInput)
    val newTextPane = TextPane(newLayout.width, newLayout.height, newToolBar.height, textInput, dispatcher, recorderQueue, storageDirectory)

    newLayout.children = Seq(newTextPane, newToolBar)

    newLayout


