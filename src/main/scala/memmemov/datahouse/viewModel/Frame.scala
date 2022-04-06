package memmemov.datahouse.viewModel

import memmemov.datahouse.{model, viewModel}

case class Frame(data: model.Frame):

  val words: Map[Int, Word] = data.words.map(
    (number, wordModel) => (number.value, Word(wordModel))
  )

  def toModel: model.Frame =
    model.Frame(
      words.map((numberValue, wordViewModel) => (model.Number(numberValue), wordViewModel.toModel))
    )

  def addWord(wordModel: model.Word): Frame =
    val newKey = model.Number(maxKey.map(_ + 1).getOrElse(1))
    val newWords = data.words + (newKey -> wordModel)
    Frame(data.copy(words = newWords))

  def lastWord: Option[Word] =
    maxKey.map(words(_))

  def isEmpty: Boolean =
    words.isEmpty

  private def maxKey: Option[Int] =
    words.keys.foldLeft(Option.empty) { (maxKey, key) =>
      maxKey match
        case None => Some(key)
        case Some(k) => if key > k then Some(key) else Some(k)
    }