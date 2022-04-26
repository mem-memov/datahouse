package memmemov.datahouse.view

import javafx.geometry.NodeOrientation
import memmemov.datahouse.model.ForwardWordReference
import memmemov.datahouse.viewModel.Word
import memmemov.datahouse.{model, viewModel}
import scalafx.Includes.*
import scalafx.scene.Group
import scalafx.scene.paint.Color.{Black, DarkGray, Gray, LightGray, White, color}
import scalafx.scene.text.{Font, Text}
import scalafx.scene.shape.Rectangle
import scalafx.scene.control.Label

class WordDisplay(
  val view: Group,
  val wordViewModel: viewModel.Word,
  val frameViewModel: viewModel.Frame,
  val storyViewModel: viewModel.Story,
  val selectionViewModel: viewModel.Selection
):
  var dragOffsetX: Double = 0
  var dragOffsetY: Double = 0
  var isDragging: Boolean = false

  view.onMousePressed = mouseEvent =>
    mouseEvent.consume()
    isDragging = false
    dragOffsetX = mouseEvent.getX - wordViewModel.position.cornerHorizontal.value
    dragOffsetY = mouseEvent.getY - wordViewModel.position.cornerVertical.value

  view.onMouseDragged = mouseEvent =>
    mouseEvent.consume()
    isDragging = true
    wordViewModel.position.moveToCornerHorizontal(mouseEvent.getX - dragOffsetX)
    wordViewModel.position.moveToCornerVertical(mouseEvent.getY - dragOffsetY)

  view.onMouseReleased = mouseEvent =>
    mouseEvent.consume()
    dragOffsetX = 0
    dragOffsetY = 0

    view.onMouseClicked = mouseEvent =>
      if !isDragging then
        mouseEvent.consume()
        val forwardWordReference = ForwardWordReference(
          storyIdentifier = storyViewModel.storyModel.identifier,
          frameNumber = frameViewModel.number.value,
          wordNumber = wordViewModel.number
        )
        selectionViewModel.forwardWordReferences.value.addOne(forwardWordReference)
        storyViewModel.startNewBlankFrame()


object WordDisplay:

  def apply(
    word: viewModel.Word,
    frame: viewModel.Frame,
    story: viewModel.Story,
    selectionViewModel: viewModel.Selection
  ): WordDisplay =

    val textItem = new Text {
      text <== word.letters
      x <== word.position.cornerHorizontal
      y <== word.position.cornerVertical
      stroke = White
      font = new Font("Arial", 20)
      fill = White
    }

    val rectangleItem = new Rectangle {
      fill = Black
      x <== word.position.cornerHorizontal - 5
      y <== word.position.cornerVertical - textItem.layoutBounds.value.getHeight - 5
      width = textItem.layoutBounds.value.getWidth + 10
      height <== textItem.layoutBounds.value.getHeight + 10
      arcWidth = 10
      arcHeight = 10
      opacity = 100
    }

    val group = new Group {
      children = Seq(rectangleItem, textItem)
    }

    word.letters.addListener{ letters =>
      rectangleItem.setWidth(textItem.layoutBounds.value.getWidth + 10)
    }

    new WordDisplay(group, word, frame, story, selectionViewModel)
