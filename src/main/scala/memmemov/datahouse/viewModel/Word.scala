package memmemov.datahouse.viewModel

import scalafx.beans.property.StringProperty
import memmemov.datahouse.model
import memmemov.datahouse.model.{BackwardWordReference, ForwardWordReference}

trait Word:

  val number: model.Number
  val letters: StringProperty
  val position: Position
  val forwardWordReferences: List[ForwardWordReference]
  val backwardWordReferences: List[BackwardWordReference]

  def toModel: model.Word =
    model.Word(
      number,
      letters.value,
      position.toModel,
      forwardWordReferences,
      backwardWordReferences
    )

object Word:

  def fromModel(data: model.Word, center: PaneCenter): Word =
    new Word:
      override val number: model.Number = data.number
      override val letters = new StringProperty(this, "letters", data.letters)
      override val position: Position = Position.fromModel(data.position, center)
      override val forwardWordReferences: List[ForwardWordReference] = data.forwardWordReferences
      override val backwardWordReferences: List[BackwardWordReference] = data.backwardWordReferences
