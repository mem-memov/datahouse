package memmemov.datahouse.view

import scalafx.Includes.*
import memmemov.datahouse.view
import memmemov.datahouse.viewModel.TextInput
import scalafx.scene.Scene
import scalafx.scene.layout.VBox

object Layout {
  def apply(containingScene: Scene, textInput: TextInput) = new VBox { vb =>
    minWidth <== containingScene.width
    minHeight <== containingScene.height
    maxWidth <== minWidth
    maxHeight <== minHeight

    val toolBar = view.ToolBar(vb, textInput)
    children = Seq(view.TextPane(vb, toolBar.height, textInput), toolBar)
  }
}
