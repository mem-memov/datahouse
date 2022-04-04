package memmemov.datahouse.userInterface

import scalafx.Includes._
import scalafx.scene.control.{TextField, TextInputControl, ToolBar}
import scalafx.scene.layout.Pane

object ToolBar {
  def apply(container: Pane, heightInContainer: Double) =
    new ToolBar { self =>
      minWidth <== container.width
      minHeight = heightInContainer
      maxWidth <== minWidth
      maxHeight <== minHeight
      content = Seq(
        new TextField {

        }
      )
    }
}
