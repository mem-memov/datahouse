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
    var frameViewModel = viewModel.Frame.fromModel(
      model.Frame(Map.empty[model.Number, model.Word])
    )
    var storyModel = model.Story(model.Identifier(UUID.randomUUID()), Map.empty[model.Number, model.Frame])
    var number = model.Number(1)
    var isNewFrame = true

    val pane = new Pane {
      minWidth <== containerWidth
      minHeight <== containerHeight - bottomOffset
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

    pane.onMousePressed = event =>
      val textValue = textInput.inputProperty.value
      if textValue.isBlank then {
        pane.setStyle("-fx-background-color: yellow")
//        thread = new Thread(() => {
//          recorder.startRecording("/tmp/voice.wav")
//        })
//        thread.start()

        dispatcher.unsafeRunSync(IO(println("button down")) >> recorderQueue.offer(Option(StartButtonMessage("/tmp/voice.wav"))))
      }


    pane.onMouseReleased = event =>
//      if thread != null then {
//        pane.setStyle("-fx-background-color: white")
//        recorder.stopRecording()
//        thread.join()
//        thread = null
//      }
      pane.setStyle("-fx-background-color: white")
      dispatcher.unsafeRunSync(IO(println("button up")) >> recorderQueue.offer(Option(StopButtonMessage())))

    pane

