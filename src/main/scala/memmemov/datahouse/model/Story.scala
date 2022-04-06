package memmemov.datahouse.model

case class Story(identifier: Identifier, frames: Map[Number, Frame]):

  def hasNumber(number: Number): Boolean =
    frames.keys.exists(number.isEqual)

  private def maxKey: Option[Number] =
    frames.keys.foldLeft(Option.empty[Number]) { (maxNumber, number) =>
      maxNumber match
        case None => Some(number)
        case Some(currentNumber) => if number.value > currentNumber.value then Some(number) else Some(currentNumber)
    }
