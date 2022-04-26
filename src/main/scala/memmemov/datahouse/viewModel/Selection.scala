package memmemov.datahouse.viewModel

import memmemov.datahouse.model.ForwardWordReference
import scalafx.beans.property.SetProperty
import scalafx.collections.ObservableSet

trait Selection:

  val forwardWordReferences: SetProperty[ForwardWordReference]

object Selection:

  def apply() =
    new Selection {
      override val forwardWordReferences: SetProperty[ForwardWordReference] = new SetProperty[ForwardWordReference](ObservableSet.empty)
    }