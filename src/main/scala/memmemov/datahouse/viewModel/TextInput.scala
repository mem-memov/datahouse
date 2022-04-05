package memmemov.datahouse.viewModel

import scalafx.beans.property.StringProperty

class TextInput (inputValue: String):
  self =>
    val inputProperty =  new StringProperty(self, "inputProperty", inputValue)

object TextInput:
  def apply(inputValue: String) = new TextInput(inputValue)