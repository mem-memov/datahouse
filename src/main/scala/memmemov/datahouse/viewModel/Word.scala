package memmemov.datahouse.viewModel

import scalafx.beans.property.StringProperty
import memmemov.datahouse.model

case class Word(data: model.Word):
  val letters = new StringProperty(this, "letters", data.letters)
  val position: Position = Position(data.position)
  def toModel: model.Word =
    model.Word(
      letters.value,
      position.toModel
    )
