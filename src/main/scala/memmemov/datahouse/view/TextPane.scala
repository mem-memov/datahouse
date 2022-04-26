package memmemov.datahouse.view

import cats.effect.{FiberIO, IO}
import cats.effect.std.{Dispatcher, Queue}
import javafx.collections.MapChangeListener
import memmemov.datahouse.configuration.StorageDirectory
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

class TextPane(
  val view: Pane,
  val storyViewModel: viewModel.Story,
  val textInput: viewModel.TextInput,
  val paneCenterViewModel: viewModel.PaneCenter,
  val selectionViewModel: viewModel.Selection,
  val dispatcher: Dispatcher[IO],
  val recorderQueue: Queue[IO, Option[ButtonMessage]],
  val storageDirectory: StorageDirectory
):
  view.onMouseClicked = event =>
    val textValue = textInput.inputProperty.value
    if textValue.nonEmpty then
      event.consume()
      textInput.inputProperty.value = ""
      storyViewModel.addWordToCurrentFrame(
        textValue,
        paneCenterViewModel.fromHorizontalCorner(event.getX).toInt,
        paneCenterViewModel.fromVerticalCorner(event.getY).toInt
      )

  view.onScroll = event =>
    val isForward = event.getDeltaY > 0
    val isBackward = event.getDeltaY < 0
    if isForward then
      storyViewModel.toNextFrame
    if isBackward then
      storyViewModel.toPreviousFrame

  view.onMousePressed = event =>
    val textValue = textInput.inputProperty.value
    if textValue.isBlank then {
      event.consume()
      view.setStyle("-fx-background-color: dark-grey")
      val filePath = s"${storageDirectory.value}/${UUID.randomUUID()}.wav"
      dispatcher.unsafeRunSync(recorderQueue.offer(Option(StartButtonMessage(filePath))))
    }

  view.onMouseReleased = event =>
    val textValue = textInput.inputProperty.value
    if textValue.isBlank then
      event.consume()
      view.setStyle("-fx-background-color: black")
      storyViewModel.addWordToCurrentFrameAndUseLetters(
        "ждите...",
        paneCenterViewModel.fromHorizontalCorner(event.getX).toInt,
        paneCenterViewModel.fromVerticalCorner(event.getY).toInt
      ) { letters =>
        dispatcher.unsafeRunSync(
          recorderQueue.offer(
            Option(StopButtonMessage(letters))
          )
        )
      }

object TextPane:

  def apply(
    containerWidth: ReadOnlyDoubleProperty,
    containerHeight: ReadOnlyDoubleProperty,
    bottomOffset: ReadOnlyDoubleProperty,
    textInput: viewModel.TextInput,
    dispatcher: Dispatcher[IO],
    recorderQueue: Queue[IO, Option[ButtonMessage]],
    storageDirectory: StorageDirectory
  ) =
    val storyModel = model.Story(model.Identifier(UUID.randomUUID()), Map.empty[model.Number, model.Frame])
    val number = model.Number(1)

    val pane = new Pane {
      style = "-fx-background-color: black"
      minWidth <== containerWidth
      minHeight <== containerHeight - bottomOffset
      maxWidth <== minWidth
      maxHeight <== minHeight
    }

    val paneCenterViewModel = viewModel.PaneCenter(pane.width, pane.height)

    val frameViewModel = viewModel.Frame.fromModel(
      model.Frame(number, Map.empty[model.Number, model.Word]),
      paneCenterViewModel
    )

    val storyViewModel = viewModel.Story.fromModel(number, storyModel, frameViewModel)

    val selectionViewModel = viewModel.Selection()

//    var subscription =
    frameViewModel.words.onChange {
      pane.children.clear()
      frameViewModel.words.value.map { (numberModel, wordViewModel) =>
        val wordDisplay = WordDisplay(wordViewModel, frameViewModel, storyViewModel, selectionViewModel)
        pane.getChildren.addOne(wordDisplay.view)
      }
    }

    new TextPane(
      pane,
      storyViewModel,
      textInput,
      paneCenterViewModel,
      selectionViewModel,
      dispatcher,
      recorderQueue,
      storageDirectory
    )

