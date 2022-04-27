package memmemov.datahouse.viewModel

import memmemov.datahouse.model
import scalafx.beans.property.SetProperty
import scalafx.collections.ObservableSet

trait Selection:

  val forwardWordReferences: SetProperty[model.ForwardWordReference]
  val backwardWordReferences: SetProperty[model.BackwardWordReference]


object Selection:

  def apply() =
    new Selection {
      override val forwardWordReferences: SetProperty[model.ForwardWordReference] = new SetProperty[model.ForwardWordReference](ObservableSet.empty)
      override val backwardWordReferences: SetProperty[model.BackwardWordReference] = new SetProperty[model.BackwardWordReference](ObservableSet.empty)
    }