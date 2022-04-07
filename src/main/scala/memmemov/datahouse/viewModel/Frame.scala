package memmemov.datahouse.viewModel

import scalafx.Includes._
import memmemov.datahouse.{model, viewModel}
import scalafx.beans.property.MapProperty

case class Frame(private var data: model.Frame):

  val words: MapProperty[model.Number, Word] =
    val mapProperty = new MapProperty[model.Number, Word]()
    data.words.foreach((numberModel, wordModel) => mapProperty.put(numberModel, Word(wordModel)))
    mapProperty

  def toModel: model.Frame =
    val wordModels = words.value.toMap.map {
      case (numberModel, word) => (numberModel, word.toModel)
    }
    model.Frame(wordModels)

  def addWord(wordModel: model.Word): Frame =
    val newKey = model.Number(maxKey.map(_ + 1).getOrElse(1))
    val newWords = data.words + (newKey -> wordModel)
    Frame(data.copy(words = newWords))

  def lastWord: Option[Word] =
    maxKey.map(words(_))

  def isEmpty: Boolean = data.words.isEmpty


  private def maxKey: Option[Int] =
    words.keys.foldLeft(Option.empty) { (maxKey, key) =>
      maxKey match
        case None => Some(key)
        case Some(k) => if key > k then Some(key) else Some(k)
    }