package memmemov.datahouse.viewModel

import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.beans.property.DoubleProperty

class PaneCenter(
  private val widthProperty: ReadOnlyDoubleProperty,
  private val heightProperty: ReadOnlyDoubleProperty
):
  self =>
    val topOffsetProperty =  new DoubleProperty(self, "topOffsetProperty", 0)
    val leftOffsetProperty =  new DoubleProperty(self, "leftOffsetProperty", 0)
    topOffsetProperty <== heightProperty / 2
    leftOffsetProperty <== widthProperty / 2

    def fromVerticalCenter(centerVertical: Double): Double = topOffsetProperty.value - centerVertical
    def fromHorizontalCenter(centerHorizontal: Double): Double = leftOffsetProperty.value + centerHorizontal

    def fromVerticalCorner(cornerVertical: Double): Double = topOffsetProperty.value - cornerVertical
    def fromHorizontalCorner(cornerHorizontal: Double): Double = cornerHorizontal - leftOffsetProperty.value

object PaneCenter:
  def apply(widthProperty: ReadOnlyDoubleProperty, heightProperty: ReadOnlyDoubleProperty) =

    new PaneCenter(widthProperty, heightProperty)