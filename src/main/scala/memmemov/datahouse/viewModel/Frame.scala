package memmemov.datahouse.viewModel

import scalafx.Includes._
import memmemov.datahouse.{model, viewModel}
import scalafx.beans.property.MapProperty
import scalafx.collections.ObservableMap

trait Frame:

  val paneCenter: PaneCenter
  val words: MapProperty[model.Number, Word]

  def addWord(wordModel: model.Word): (model.Number, Word) =
    val newNumber = maxKey.map(_.increment).getOrElse(model.Number(1))
    val newWord = Word.fromModel(wordModel, paneCenter: PaneCenter)
    words.put(newNumber, newWord)
    (newNumber, newWord)

  def isEmpty: Boolean = words.value.isEmpty
  def nonEmpty: Boolean = words.value.nonEmpty

  def toModel: model.Frame =
    val wordModels = words.value.toMap.map {
      case (numberModel, word) => (numberModel, word.toModel)
    }
    model.Frame(wordModels)

  private def maxKey: Option[model.Number] =
    words.value.keys.toList.foldLeft(Option.empty) { (maxKey, key) =>
      maxKey match
        case None => Some(key)
        case Some(k) => if key.value > k.value then Some(key) else Some(k)
    }

object Frame:

  def fromModel(data: model.Frame, center: PaneCenter): Frame =
    val mapWithWords = data.words.map { (numberModel, wordModel) =>
      (numberModel, Word.fromModel(wordModel, center))
    }
    val observableMap = ObservableMap.from(mapWithWords)
    new Frame:
      override val paneCenter: PaneCenter = center
      override val words: MapProperty[model.Number, Word] = MapProperty(this, "words", observableMap)