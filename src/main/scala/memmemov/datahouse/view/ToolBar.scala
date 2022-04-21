package memmemov.datahouse.view

import memmemov.datahouse.viewModel.TextInput
import scalafx.Includes.*
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.beans.property.DoubleProperty
import scalafx.scene.control.{TextField, TextInputControl, ToolBar, ToggleButton}
import scalafx.scene.layout.Pane

object ToolBar:

  def apply(
    containerWidth: ReadOnlyDoubleProperty,
    textInput: TextInput
  ): ToolBar =

    val textField = new TextField {
      text <==> textInput.inputProperty
    }

    val microphoneButton = new ToggleButton {
      text = (0x1f603).toChar.toString
    }


    new ToolBar { self =>
      minWidth <== containerWidth
      maxWidth <== minWidth
      maxHeight <== minHeight
      content = Seq(
        textField,
        microphoneButton
      )
    }
