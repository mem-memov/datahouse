package memmemov.datahouse.viewModel

import memmemov.datahouse.model
import memmemov.datahouse.model.ForwardWordReference
import scalafx.beans.property.StringProperty

class Story(
  var numberModel: model.Number,
  var storyModel: model.Story,
  val frame: Frame,
  val selection: Selection
):
  def toModel: model.Story = storyModel

  def startNewBlankFrame(): Unit =
    frame.words.clear()
    numberModel = storyModel.maxKey match
      case None => model.Number(1)
      case Some(n) => n.increment

  def addWordToCurrentFrame(textValue: String, horizontal: Int, vertical: Int): Unit =
    // new word
    val wordNumber = frame.maxKey.map(_.increment).getOrElse(model.Number(1))
    val wordModel = selection.backwardWordReferences.value.foldLeft(
      model.Word.fromLettersAndCoordinates(wordNumber, textValue, horizontal, vertical)
    ) { (wordModel, backwardWordReference) =>
      storyModel.findWordByReference(backwardWordReference) match
        case None =>
          wordModel
        case Some(existingWord) =>
          val newWordBackwardWordReferences = wordModel.backwardWordReferences.appended(backwardWordReference)
          wordModel.copy(backwardWordReferences = newWordBackwardWordReferences)
    }
    frame.addWord(wordModel)
    storyModel = frame.updateStory(numberModel, storyModel)

    // existing word
    storyModel = selection.backwardWordReferences.value.foldLeft(storyModel) { (storyModel, backwardWordReference) =>
      storyModel.findWordByReference(backwardWordReference) match
        case None => storyModel
        case Some(existingWord) =>
          val forwardWordReference = ForwardWordReference(storyModel.identifier, numberModel, model.Number(1))
          val existingWordForwardWordReferences = existingWord.forwardWordReferences.appended(forwardWordReference)
          val updatedExistingWord = existingWord.copy(forwardWordReferences = existingWordForwardWordReferences)
          storyModel.replaceWordAtReference(updatedExistingWord, backwardWordReference)
    }
    selection.forwardWordReferences.value.clear()
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

  def fromModel(numberModel: model.Number, storyModel: model.Story, frame: Frame, selection: Selection): Story =

    new Story(numberModel, storyModel, frame, selection)
