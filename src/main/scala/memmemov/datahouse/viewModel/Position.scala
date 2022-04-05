package memmemov.datahouse.viewModel

import scalafx.beans.property.IntegerProperty
import memmemov.datahouse.model

case class Position(data: model.Position):
  val horizontal = new IntegerProperty(this, "horizontal", data.horizontal.value)
  val vertical = new IntegerProperty(this, "vertical", data.vertical.value)
  def toModel: model.Position =
    model.Position(
      model.Coordinate(horizontal.value),
      model.Coordinate(vertical.value)
    )
