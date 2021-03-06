package memmemov.datahouse.viewModel

import scalafx.Includes._
import memmemov.datahouse.{model, viewModel}
import scalafx.beans.property.{MapProperty, ObjectProperty}
import scalafx.collections.ObservableMap

trait Frame:

  val paneCenter: PaneCenter
  val number: ObjectProperty[model.Number]
  val words: MapProperty[model.Number, Word]

  def addWord(wordModel: model.Word): Word =
    val newWord = Word.fromModel(wordModel, paneCenter: PaneCenter)
    words.put(wordModel.number, newWord)
    newWord

  def isEmpty: Boolean = words.value.isEmpty
  def nonEmpty: Boolean = words.value.nonEmpty

  def toModel: model.Frame =
    val wordModels = words.value.toMap.map {
      case (numberModel, word) => (numberModel, word.toModel)
    }
    model.Frame(number.value, wordModels)

  def maxKey: Option[model.Number] =
    words.value.keys.toList.foldLeft(Option.empty) { (maxKey, key) =>
      maxKey match
        case None => Some(key)
        case Some(k) => if key.value > k.value then Some(key) else Some(k)
    }

  def updateStory(numberModel: model.Number, storyModel: model.Story): model.Story =
    if words.value.nonEmpty then 
      val newFrames = storyModel.frames.updated(numberModel, toModel)
      storyModel.copy(frames = newFrames)
    else
      storyModel

  def appendToStory(numberModel: model.Number, storyModel: model.Story): model.Story =
    if words.value.nonEmpty then 
      val newFrames = storyModel.frames.updated(numberModel, toModel)
      storyModel.copy(frames = newFrames)
    else
      storyModel

  def loadFrameFromStory(numberModel: model.Number, storyModel: model.Story): Unit =
    val frameModel = storyModel.frames(numberModel)
    words.clear()
    frameModel.words.foreach { (number, word) => addWord(word) }

object Frame:

  def fromModel(data: model.Frame, center: PaneCenter): Frame =
    val mapWithWords = data.words.map { (numberModel, wordModel) =>
      (numberModel, Word.fromModel(wordModel, center))
    }
    val observableMap = ObservableMap.from(mapWithWords)
    new Frame:
      override val paneCenter: PaneCenter = center
      override val number: ObjectProperty[model.Number] = ObjectProperty(this, "number", data.number)
      override val words: MapProperty[model.Number, Word] = MapProperty(this, "words", observableMap)