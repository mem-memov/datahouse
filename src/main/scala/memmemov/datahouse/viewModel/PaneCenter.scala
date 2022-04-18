package memmemov.datahouse.viewModel

import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.beans.property.DoubleProperty

class PaneCenter(
  private val widthProperty: ReadOnlyDoubleProperty,
  private val heightProperty: ReadOnlyDoubleProperty
):
  self =>
    val topOffsetProperty =  new DoubleProperty(self, "xProperty", 0)
    val leftOffsetProperty =  new DoubleProperty(self, "yProperty", 0)
    topOffsetProperty <== widthProperty / 2
    leftOffsetProperty <== heightProperty / 2
    topOffsetProperty.addListener(value => println(s"top offset: $value"))

object PaneCenter:
  def apply(widthProperty: ReadOnlyDoubleProperty, heightProperty: ReadOnlyDoubleProperty) =

    new PaneCenter(widthProperty, heightProperty)