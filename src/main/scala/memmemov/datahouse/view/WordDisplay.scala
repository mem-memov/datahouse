package memmemov.datahouse.view

import memmemov.datahouse.viewModel
import scalafx.Includes._
import scalafx.scene.Group
import scalafx.scene.paint.Color.{Black, DarkGray, Gray, LightGray, color}
import scalafx.scene.text.{Font, Text}
import scalafx.scene.shape.Rectangle
import scalafx.scene.control.Label

object WordDisplay:
  def apply(word: viewModel.Word) =

    val textItem = new Text {
      text <== word.letters
      x <== word.position.horizontal
      y <== word.position.vertical
      stroke = Gray
      font = new Font("Arial", 20)
      fill = Black
    }

    val rectangleItem = new Rectangle {
      fill = LightGray
      x <== word.position.horizontal - 5
      y <== word.position.vertical - textItem.layoutBounds.value.getHeight - 5
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

    var dragOffsetX: Double = 0
    var dragOffsetY: Double = 0

    group.onMousePressed = mouseEvent =>
      mouseEvent.consume()
      dragOffsetX = mouseEvent.getX - word.position.horizontal.value
      dragOffsetY = mouseEvent.getY - word.position.vertical.value

    group.onMouseDragged = mouseEvent =>
      mouseEvent.consume()
      word.position.horizontal.value = mouseEvent.getX - dragOffsetX
      word.position.vertical.value = mouseEvent.getY - dragOffsetY

    group.onMouseReleased = mouseEvent =>
      mouseEvent.consume()

    group