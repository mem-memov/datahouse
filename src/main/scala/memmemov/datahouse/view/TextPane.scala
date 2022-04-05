package memmemov.datahouse.view

import memmemov.datahouse.viewModel
import memmemov.datahouse.model
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.Includes.*
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{Font, Text}
import scalafx.event.Event

object TextPane {
  def apply(container: Pane, bottomOffset: ReadOnlyDoubleProperty, textInput: viewModel.TextInput) =
    new Pane { self =>
      minWidth <== container.width
      minHeight <== container.height - bottomOffset
      maxWidth <== minWidth
      maxHeight <== minHeight

      children = Seq(

      )
      onMouseClicked = event =>

        val textValue = textInput.inputProperty.value

        if !textValue.isEmpty then
          textInput.inputProperty.value = ""
          self.getChildren.addOne(
            WordDisplay(
              viewModel.Word(
                model.Word(
                  letters = textValue,
                  position = model.Position(
                    horizontal = model.Coordinate(event.getX.toInt),
                    vertical = model.Coordinate(event.getY.toInt)
                  )
                )
              )
            )
          )
    }
}
