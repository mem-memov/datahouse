package memmemov.datahouse.userInterface

import scalafx.Includes._
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color._
import scalafx.scene.text.{Font, Text}

object TextPane {
  def apply(container: Pane, bottomOffset: Double) =
    new Pane { p =>
      minWidth <== container.width
      minHeight <== container.height - bottomOffset
      maxWidth <== minWidth
      maxHeight <== minHeight

      children = Seq(

      )
      onMouseClicked = event =>
        p.getChildren.addOne(
          new Text("machine") {
            x = event.getX
            y = event.getY
            stroke = Gray
            font = new Font("Arial", 20)
            fill <== when(hover) choose LightGray otherwise DarkGray
          }
        )

    }
}
