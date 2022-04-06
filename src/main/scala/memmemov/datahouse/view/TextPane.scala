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

    var number = model.Number(1)

    val pane = new Pane {
      minWidth <== container.width
      minHeight <== container.height - bottomOffset
      maxWidth <== minWidth
      maxHeight <== minHeight
    }

    def clearFrame() =
      pane.children.clear()
      frameViewModel = viewModel.Frame(model.Frame.empty)

    def setFrameModel(frameModel: model.Frame) =
      clearFrame()
      frameModel.words.foreach { (number, word) =>
        addWordModel(word)
      }

    def addWordModel(wordModel: model.Word) =
      frameViewModel = frameViewModel.addWord(wordModel)

      frameViewModel.lastWord match
        case None => ()
        case Some(word) =>
          pane.getChildren.addOne(
            WordDisplay(word)
          )

    def appendCurrentFrameToStory(n: model.Number) =
      val frameModel = frameViewModel.toModel
      val newFrames = storyModel.frames.updated(number, frameModel)
      storyModel = storyModel.copy(frames = newFrames)
      clearFrame()

    def loadFrameFromStory(n: model.Number) =
      val frameModel = storyModel.frames(n)
      setFrameModel(frameModel)

    pane.onMouseClicked = event =>

      val textValue = textInput.inputProperty.value

      if !textValue.isEmpty then
        textInput.inputProperty.value = ""

        val wordModel = model.Word.fromLettersAndCoordinates(textValue, event.getX.toInt, event.getY.toInt)
        addWordModel(wordModel)

    pane.onScroll = event =>

      val isForward = event.getDeltaY > 0
      val isBackward = event.getDeltaY < 0

      if isForward then {
        if storyModel.hasNumber(number.increment) then {
          loadFrameFromStory(number.increment)
          number = number.increment
        } else {
          if !frameViewModel.isEmpty then {
            appendCurrentFrameToStory(number)
            number = number.increment
          }
        }
      }

      if isBackward then {
        if !frameViewModel.isEmpty && !storyModel.hasNumber(number) then {
          appendCurrentFrameToStory(number)
        }
        number = number.decrement
        if storyModel.hasNumber(number) then {
          loadFrameFromStory(number)
        }
      }

    pane
}
