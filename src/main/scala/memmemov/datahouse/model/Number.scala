package memmemov.datahouse.model

case class Number(value: Int): //extends AnyVal:
  def increment: Number = Number(value + 1)
  def decrement: Number = if value > 1 then Number(value - 1) else this
  def isEqual(another: Number): Boolean = this.value == another.value
