package memmemov.datahouse.memory

class Page (val size: Int):
  private val bytes = new Array[Byte](size)

  def put(position: Int, byte: Byte): Unit =
    bytes.update(position, byte)

  def get(position: Int): Byte =
    bytes(position)

