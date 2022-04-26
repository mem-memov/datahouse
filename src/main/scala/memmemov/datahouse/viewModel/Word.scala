package memmemov.datahouse.viewModel

import scalafx.beans.property.StringProperty
import memmemov.datahouse.model
import memmemov.datahouse.model.{BackwardWordReference, ForwardWordReference}

trait Word:

  val number: model.Number
  val letters: StringProperty
  val position: Position

  def toModel: model.Word =
    model.Word(
      number,
      letters.value,
      position.toModel,
      List.empty[ForwardWordReference],
      List.empty[BackwardWordReference]
    )

object Word:

  def fromModel(data: model.Word, center: PaneCenter): Word =
    new Word:
      override val number: model.Number = data.number
      override val letters = new StringProperty(this, "letters", data.letters)
      override val position: Position = Position.fromModel(data.position, center)
