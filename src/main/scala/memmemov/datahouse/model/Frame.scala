package memmemov.datahouse.model

case class Frame(number: Number, words: Map[Number, Word]):

  def maxKey: Option[Number] =
    words.keys.foldLeft(Option.empty[Number]) { (maxNumber, number) =>
      maxNumber match
        case None => Some(number)
        case Some(currentNumber) => if number.value > currentNumber.value then Some(number) else Some(currentNumber)
    }

object Frame:
  def empty(number: Number): Frame =
    Frame(number, Map.empty[Number, Word])
