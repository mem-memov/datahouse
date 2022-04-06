package memmemov.datahouse.model

case class Frame(words: Map[Number, Word])

object Frame:
  def empty: Frame =
    Frame(Map.empty[Number, Word])
