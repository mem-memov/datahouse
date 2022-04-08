package memmemov.datahouse.viewModel

import scalafx.beans.property.DoubleProperty
import memmemov.datahouse.model

trait Position:

  val horizontal:DoubleProperty
  val vertical: DoubleProperty

  def toModel: model.Position =
    model.Position(
      model.Coordinate(horizontal.value.toInt),
      model.Coordinate(vertical.value.toInt)
    )

object Position:

  def fromModel(data: model.Position): Position =
    new Position:
      override val horizontal: DoubleProperty = new DoubleProperty(this, "horizontal", data.horizontal.value.toDouble)
      override val vertical: DoubleProperty = new DoubleProperty(this, "vertical", data.vertical.value.toDouble)

