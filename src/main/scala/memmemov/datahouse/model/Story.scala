package memmemov.datahouse.model

case class Story(identifier: Identifier, frames: Map[Number, Frame]):

  def hasNumber(number: Number): Boolean =
    frames.keys.exists(number.isEqual)

  def maxKey: Option[Number] =
    frames.keys.foldLeft(Option.empty[Number]) { (maxNumber, number) =>
      maxNumber match
        case None => Some(number)
        case Some(currentNumber) => if number.value > currentNumber.value then Some(number) else Some(currentNumber)
    }

  def findWordByReference(wordReference: WordReference): Option[Word] =
    def findWord(storyIdentifier: Identifier, frameNumber: Number, wordNumber: Number): Option[Word] =
      if storyIdentifier.value != identifier.value then
        Option.empty
      else
        for {
          frame <- frames.get(frameNumber)
          word <- frame.words.get(wordNumber)
        } yield word

    wordReference match {
      case ForwardWordReference(storyIdentifier, frameNumber, wordNumber) => findWord(storyIdentifier, frameNumber, wordNumber)
      case BackwardWordReference(storyIdentifier, frameNumber, wordNumber) => findWord(storyIdentifier, frameNumber, wordNumber)
    }

  def replaceWordAtReference(newWord: Word, wordReference: WordReference): Story =

    def replaceWord(storyIdentifier: Identifier, frameNumber: Number, wordNumber: Number): Story =
      if storyIdentifier.value != identifier.value then
        this
      else
        val optionalExistingFrameAndWord: Option[(Frame, Word)] = for {
          existingFrame <- frames.get(frameNumber)
          existingWord <- existingFrame.words.get(wordNumber)
        } yield (existingFrame, existingWord)

        optionalExistingFrameAndWord match
          case Some(existingFrame, existingWord) =>
            val updatedFrameWords = existingFrame.words.updated(wordNumber, newWord)
            val updatedFrame = existingFrame.copy(words = updatedFrameWords)
            val updatedStoryFrames = this.frames.updated(frameNumber, updatedFrame)
            this.copy(frames = updatedStoryFrames)
          case _ => this

    wordReference match {
      case ForwardWordReference(storyIdentifier, frameNumber, wordNumber) => replaceWord(storyIdentifier, frameNumber, wordNumber)
      case BackwardWordReference(storyIdentifier, frameNumber, wordNumber) => replaceWord(storyIdentifier, frameNumber, wordNumber)
    }
