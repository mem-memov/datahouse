package memmemov.datahouse.view

import cats.effect.{FiberIO, IO}
import cats.effect.std.{Dispatcher, Queue}
import javafx.collections.MapChangeListener
import memmemov.datahouse.viewModel
import memmemov.datahouse.model
import memmemov.datahouse.speech.{ButtonMessage, Recorder, StartButtonMessage, StopButtonMessage}
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.Includes.*
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{Font, Text}
import scalafx.event.Event

import java.util.UUID
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object TextPane:

  def apply(
   containerWidth: ReadOnlyDoubleProperty,
   containerHeight: ReadOnlyDoubleProperty,
   bottomOffset: ReadOnlyDoubleProperty,
   textInput: viewModel.TextInput,
   dispatcher: Dispatcher[IO],
   recorderQueue: Queue[IO, Option[ButtonMessage]]
  ) =

    var storyModel = model.Story(model.Identifier(UUID.randomUUID()), Map.empty[model.Number, model.Frame])
    var number = model.Number(1)

    val pane = new Pane {
      style = "-fx-background-color: black"
      minWidth <== containerWidth
      minHeight <== containerHeight - bottomOffset
      maxWidth <== minWidth
      maxHeight <== minHeight
    }

    val paneCenterViewModel = viewModel.PaneCenter(pane.width, pane.height)

    val frameViewModel = viewModel.Frame.fromModel(
      model.Frame(Map.empty[model.Number, model.Word]),
      paneCenterViewModel
    )

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
        event.consume()
        textInput.inputProperty.value = ""

        val wordModel = model.Word.fromLettersAndCoordinates(
          textValue,
          paneCenterViewModel.fromHorizontalCorner(event.getX).toInt,
          paneCenterViewModel.fromVerticalCorner(event.getY).toInt
        )
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

    pane.onMousePressed = event =>
      val textValue = textInput.inputProperty.value
      if textValue.isBlank then {
        event.consume()
        pane.setStyle("-fx-background-color: dark-grey")
        dispatcher.unsafeRunSync(recorderQueue.offer(Option(StartButtonMessage("/home/u/Desktop/voice.wav"))))
      }

    pane.onMouseReleased = event =>
      val textValue = textInput.inputProperty.value
      if textValue.isBlank then {
        event.consume()
        pane.setStyle("-fx-background-color: black")
        val wordModel = model.Word.fromLettersAndCoordinates(
          "ждите...",
          paneCenterViewModel.fromHorizontalCorner(event.getX).toInt,
          paneCenterViewModel.fromVerticalCorner(event.getY).toInt
        )
        val (numberModel, wordViewModel) = frameViewModel.addWord(wordModel)
        dispatcher.unsafeRunSync(recorderQueue.offer(Option(StopButtonMessage(wordViewModel.letters))))
      }

    pane

