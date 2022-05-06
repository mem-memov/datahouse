package memmemov.datahouse.memory

import scala.collection.mutable.ArrayBuffer

class Book (val pageSize: Int) {
  private var pages = new ArrayBuffer[Page](0)

  private var end: Int = 0


  def create(byte: Byte): Unit =
    end = end + 1
    val pageIndex = end / pageSize
    val pagePosition = end - pageIndex * pageSize
    val pagesNeeded = pageIndex - pages.length - 1
    (0 to pagesNeeded).foreach { _ =>
      pages = pages.appended(new Page(pageSize))
    }
    val page = pages(pageIndex)
    
  def read(address: Int): Byte = ???
  def update(address: Int, byte: Byte): Unit = ???
  def delete(address: Int): Unit = ???
}
