package memmemov.datahouse.view

import memmemov.datahouse.viewModel.TextInput
import scalafx.Includes.*
import scalafx.scene.control.{TextField, TextInputControl, ToolBar}
import scalafx.scene.layout.Pane

object ToolBar {
  def apply(container: Pane, textInput: TextInput) =
    new ToolBar { self =>
      minWidth <== container.width
      maxWidth <== minWidth
      maxHeight <== minHeight
      content = Seq(
        new TextField {
          text <==> textInput.inputProperty
        }
      )
    }
}
