package memmemov.datahouse.viewModel

import scalafx.beans.property.DoubleProperty
import memmemov.datahouse.model

trait Position:

  val paneCenter: PaneCenter

  val centerHorizontal: DoubleProperty
  val centerVertical: DoubleProperty

  val cornerHorizontal: DoubleProperty
  val cornerVertical: DoubleProperty

  def moveToCornerHorizontal(cornerHorizontal: Double): Unit =
    centerHorizontal.value = cornerHorizontal - paneCenter.leftOffsetProperty.value

  def moveToCornerVertical(cornerVertical: Double): Unit =
    centerVertical.value = paneCenter.topOffsetProperty.value - cornerVertical

  def toModel: model.Position =
    model.Position(
      model.Coordinate(centerHorizontal.value.toInt),
      model.Coordinate(centerVertical.value.toInt)
    )

object Position:

  def fromModel(data: model.Position, center: PaneCenter): Position =

    val centerHorizontal_v: DoubleProperty = new DoubleProperty(this, "centerHorizontal", data.horizontal.value.toDouble)
    val centerVertical_v: DoubleProperty = new DoubleProperty(this, "centerVertical", data.vertical.value.toDouble)

    val cornerHorizontal_v: DoubleProperty = new DoubleProperty(this, "cornerHorizontal", center.fromHorizontalCenter(data.horizontal.value.toDouble))
    val cornerVertical_v: DoubleProperty = new DoubleProperty(this, "cornerVertical", center.fromVerticalCenter(data.vertical.value.toDouble))

    cornerHorizontal_v <== center.leftOffsetProperty + centerHorizontal_v
    cornerVertical_v <== center.topOffsetProperty - centerVertical_v

    new Position:
      override val paneCenter: PaneCenter = center

      override val centerHorizontal: DoubleProperty = centerHorizontal_v
      override val centerVertical: DoubleProperty = centerVertical_v

      override val cornerHorizontal: DoubleProperty = cornerHorizontal_v
      override val cornerVertical: DoubleProperty = cornerVertical_v

