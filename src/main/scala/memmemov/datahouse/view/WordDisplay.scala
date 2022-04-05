package memmemov.datahouse.view

import memmemov.datahouse.viewModel
import scalafx.Includes._
import scalafx.scene.Group
import scalafx.scene.paint.Color.{Black, DarkGray, Gray, LightGray, color}
import scalafx.scene.text.{Font, Text}
import scalafx.scene.shape.Rectangle

object WordDisplay:
  def apply(word: viewModel.Word) = new Group {
    val textItem = new Text {
      text <== word.letters
      x <== word.position.horizontal.value
      y <== word.position.vertical.value
      stroke = Gray
      font = new Font("Arial", 20)
      fill = Black
    }
    val rectangleItem = new Rectangle {
//      fill <== when(hover) choose LightGray otherwise DarkGray
      fill = LightGray
      x <== word.position.horizontal.value - 5
      y <== word.position.vertical.value - textItem.layoutBounds.value.getHeight - 5
      width <== textItem.layoutBounds.value.getWidth + 10
      height <== textItem.layoutBounds.value.getHeight + 10
      arcWidth = 10
      arcHeight = 10
    }
    children = Seq(rectangleItem, textItem)
  }
