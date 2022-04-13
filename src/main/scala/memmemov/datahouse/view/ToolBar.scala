package memmemov.datahouse.view

import memmemov.datahouse.viewModel.TextInput
import scalafx.Includes.*
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.beans.property.DoubleProperty
import scalafx.scene.control.{TextField, TextInputControl, ToolBar}
import scalafx.scene.layout.Pane

object ToolBar:

  def apply(
    containerWidth: ReadOnlyDoubleProperty,
    textInput: TextInput
  ): ToolBar =
    new ToolBar { self =>
      minWidth <== containerWidth
      maxWidth <== minWidth
      maxHeight <== minHeight
      content = Seq(
        new TextField {
          text <==> textInput.inputProperty
        }
      )
    }
