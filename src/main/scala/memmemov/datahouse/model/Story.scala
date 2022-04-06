package memmemov.datahouse.model

case class Story(identifier: Identifier, frames: Map[Number, Frame]):

  def addFrame(frame: Frame): Story =
    val newKey = maxKey.map(_.increment).getOrElse(Number(1))
    val newFrames = frames + (newKey -> frame)
    copy(frames = newFrames)

  private def maxKey: Option[Number] =
    frames.keys.foldLeft(Option.empty[Number]) { (maxNumber, number) =>
      maxNumber match
        case None => Some(number)
        case Some(currentNumber) => if number.value > currentNumber.value then Some(number) else Some(currentNumber)
    }
