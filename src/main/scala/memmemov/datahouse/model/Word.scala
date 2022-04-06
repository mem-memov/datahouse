package memmemov.datahouse.model

case class Word(letters: String, position: Position)

object Word:
  def fromLettersAndCoordinates(letters: String, horizontal: Int, vertical: Int): Word =
    apply(
      letters = letters,
      position = Position(
        horizontal = Coordinate(horizontal),
        vertical = Coordinate(vertical)
      )
    )
