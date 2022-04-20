package memmemov.datahouse.model

sealed trait WordReference

case class ForwardWordReference(storyIdentifier: Identifier, frameNumber: Number, wordNumber: Number) extends WordReference
case class BackwardWordReference(storyIdentifier: Identifier, frameNumber: Number, wordNumber: Number) extends WordReference
