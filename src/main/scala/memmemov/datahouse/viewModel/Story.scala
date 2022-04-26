package memmemov.datahouse.viewModel

import memmemov.datahouse.model
import scalafx.beans.property.StringProperty

class Story(
  var numberModel: model.Number,
  var storyModel: model.Story,
  val frame: Frame,
):
  def toModel: model.Story = storyModel

  def startNewBlankFrame(): Unit =
    frame.words.clear()
    numberModel = storyModel.maxKey match
      case None => model.Number(1)
      case Some(n) => n.increment

  def startNewFrameWithWord(wordModel: model.Word): Unit =
    storyModel = frame.updateStory(numberModel, storyModel)
    frame.words.clear()
    numberModel = storyModel.maxKey match
      case None => model.Number(1)
      case Some(n) => n.increment
    frame.addWord(wordModel)
    ()

  def addWordToCurrentFrame(textValue: String, horizontal: Int, vertical: Int): Unit =
    val wordNumber = frame.maxKey.map(_.increment).getOrElse(model.Number(1))
    val wordModel = model.Word.fromLettersAndCoordinates(wordNumber, textValue, horizontal, vertical)
    frame.addWord(wordModel)
    storyModel = frame.updateStory(numberModel, storyModel)
    ()

  def addWordToCurrentFrameAndUseLetters(textValue: String, horizontal: Int, vertical: Int)(useLetters: StringProperty => Unit): Unit =
    val wordNumber = frame.maxKey.map(_.increment).getOrElse(model.Number(1))
    val wordModel = model.Word.fromLettersAndCoordinates(wordNumber, textValue, horizontal, vertical)
    val wordVM = frame.addWord(wordModel)
    useLetters(wordVM.letters)
    storyModel = frame.updateStory(numberModel, storyModel)
    ()

  def toNextFrame: Unit =
    if storyModel.hasNumber(numberModel.increment) then
      storyModel = frame.updateStory(numberModel, storyModel)
      frame.loadFrameFromStory(numberModel.increment, storyModel)
      numberModel = numberModel.increment
    else
      if frame.nonEmpty then
        storyModel = frame.appendToStory(numberModel, storyModel)
        frame.words.clear()
        numberModel = numberModel.increment
    ()

  def toPreviousFrame: Unit =
    if !storyModel.hasNumber(numberModel.increment) then
      storyModel = frame.appendToStory(numberModel, storyModel)

    if storyModel.hasNumber(numberModel.decrement) then
      storyModel = frame.updateStory(numberModel, storyModel)
      frame.loadFrameFromStory(numberModel.decrement, storyModel)
      numberModel = numberModel.decrement
    ()

object Story:

  def fromModel(n: model.Number, s: model.Story, f: Frame): Story =

    new Story(n, s, f)
