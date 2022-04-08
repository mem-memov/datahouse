package memmemov.datahouse.viewModel

import scalafx.beans.property.StringProperty
import memmemov.datahouse.model

trait Word:

  val letters: StringProperty
  val position: Position

  def toModel: model.Word =
    model.Word(
      letters.value,
      position.toModel
    )

object Word:

  def fromModel(data: model.Word): Word =
    new Word:
      override val letters = new StringProperty(this, "letters", data.letters)
      override val position: Position = Position.fromModel(data.position)
