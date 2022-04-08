package memmemov.datahouse.view

import javafx.collections.MapChangeListener
import memmemov.datahouse.viewModel
import memmemov.datahouse.model
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.Includes.*
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{Font, Text}
import scalafx.event.Event

import java.util.UUID

object TextPane:

  def apply(
   container: Pane,
   bottomOffset: ReadOnlyDoubleProperty,
   textInput: viewModel.TextInput
  ) =

    var frameViewModel = viewModel.Frame.fromModel(
      model.Frame(Map.empty[model.Number, model.Word])
    )
    var storyModel = model.Story(model.Identifier(UUID.randomUUID()), Map.empty[model.Number, model.Frame])
    var number = model.Number(1)
    var isNewFrame = true

    val pane = new Pane {
      minWidth <== container.width
      minHeight <== container.height - bottomOffset
      maxWidth <== minWidth
      maxHeight <== minHeight
    }

//    var subscription =
    frameViewModel.words.onChange {
      pane.children.clear()
      frameViewModel.words.value.map { (numberModel, wordViewModel) =>
        pane.getChildren.addOne(
          WordDisplay(wordViewModel)
        )
      }
    }

    def appendCurrentFrameToStory(n: model.Number) =
      val frameModel = frameViewModel.toModel
      val newFrames = storyModel.frames.updated(number, frameModel)
      storyModel = storyModel.copy(frames = newFrames)

    def loadFrameFromStory(n: model.Number) =
      val frameModel = storyModel.frames(n)
      frameViewModel.words.clear()
      frameModel.words.foreach { (number, word) =>
        frameViewModel.addWord(word)
      }

    def updateStoryFrame(n: model.Number) =
      val frameModel = frameViewModel.toModel
      val newFrames = storyModel.frames.updated(number, frameModel)
      storyModel = storyModel.copy(frames = newFrames)

    pane.onMouseClicked = event =>

      val textValue = textInput.inputProperty.value

      if textValue.nonEmpty then
        textInput.inputProperty.value = ""

        val wordModel = model.Word.fromLettersAndCoordinates(textValue, event.getX.toInt, event.getY.toInt)
        val (numberModel, wordViewModel) = frameViewModel.addWord(wordModel)


    pane.onScroll = event =>

      val isForward = event.getDeltaY > 0
      val isBackward = event.getDeltaY < 0

      if isForward then {
        if storyModel.hasNumber(number.increment) then {
          updateStoryFrame(number)
          loadFrameFromStory(number.increment)
          number = number.increment
        } else {
          if frameViewModel.nonEmpty then {
            appendCurrentFrameToStory(number)
            frameViewModel.words.clear()
            number = number.increment
          }
        }
      }

      if isBackward then {
        if !storyModel.hasNumber(number.increment) then {
          appendCurrentFrameToStory(number)
        }
        if storyModel.hasNumber(number.decrement) then {
          updateStoryFrame(number)
          loadFrameFromStory(number.decrement)
          number = number.decrement
        }
      }

    pane

