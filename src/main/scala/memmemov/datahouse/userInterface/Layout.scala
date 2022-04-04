package memmemov.datahouse.userInterface

import scalafx.Includes._
import memmemov.datahouse.userInterface
import scalafx.scene.Scene
import scalafx.scene.layout.VBox

object Layout {
  def apply(scene: Scene, menuHeight: Double) = new VBox { vb =>
    minWidth <== scene.width
    minHeight <== scene.height
    maxWidth <== minWidth
    maxHeight <== minHeight
    children = Seq(userInterface.TextPane(vb, menuHeight), userInterface.ToolBar(vb, menuHeight))
  }
}
