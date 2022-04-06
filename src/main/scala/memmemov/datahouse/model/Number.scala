package memmemov.datahouse.model

case class Number(value: Int) extends AnyVal:
  def increment: Number = Number(value + 1)
