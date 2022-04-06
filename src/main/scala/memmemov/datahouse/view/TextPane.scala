package memmemov.datahouse.view

import memmemov.datahouse.viewModel
import memmemov.datahouse.model
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.Includes.*
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{Font, Text}
import scalafx.event.Event

import java.util.UUID

object TextPane {
  def apply(
   container: Pane,
   bottomOffset: ReadOnlyDoubleProperty,
   textInput: viewModel.TextInput
  ) =
    var frameViewModel = viewModel.Frame(
      model.Frame(Map.empty[model.Number, model.Word])
    )

    var storyModel = model.Story(model.Identifier(UUID.randomUUID()), Map.empty[model.Number, model.Frame])

    new Pane { self =>
      minWidth <== container.width
      minHeight <== container.height - bottomOffset
      maxWidth <== minWidth
      maxHeight <== minHeight

      children = Seq(

      )
      onMouseClicked = event =>

        val textValue = textInput.inputProperty.value

        if !textValue.isEmpty then
          textInput.inputProperty.value = ""

          val wordModel = model.Word.fromLettersAndCoordinates(textValue, event.getX.toInt, event.getY.toInt)

          frameViewModel = frameViewModel.addWord(wordModel)

          frameViewModel.lastWord match
            case None => ()
            case Some(word) =>
              self.getChildren.addOne(
                WordDisplay(word)
              )

      onScroll = event =>
        val isForward = event.getDeltaY > 0
        if isForward && !frameViewModel.isEmpty then
          val frameModel = frameViewModel.toModel
          storyModel = storyModel.addFrame(frameModel)
          frameViewModel = viewModel.Frame(model.Frame.empty)
          self.children.clear()
//        if !isForward then

    }
}
