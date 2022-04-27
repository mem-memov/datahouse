package memmemov.datahouse.model

sealed trait WordReference

case class ForwardWordReference(storyIdentifier: Identifier, frameNumber: Number, wordNumber: Number) extends WordReference:
  def toBackwardWordReference: BackwardWordReference =
    BackwardWordReference(storyIdentifier, frameNumber, wordNumber)

case class BackwardWordReference(storyIdentifier: Identifier, frameNumber: Number, wordNumber: Number) extends WordReference:
  def toForwardWordReference: ForwardWordReference =
    ForwardWordReference(storyIdentifier, frameNumber, wordNumber)
