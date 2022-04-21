package memmemov.datahouse.viewModel

import memmemov.datahouse.model
import scalafx.beans.property.StringProperty

trait Story:

  self =>

    val numberModel: model.Number
    val storyModel: model.Story
    val frame: Frame

    def toModel: model.Story = storyModel

    def startNewFrameWithWord(wordModel: model.Word): Story =
      startNewFrameWithWordAndUseLetters(wordModel)(_ => ())
      
    def startNewFrameWithWordAndUseLetters(wordModel: model.Word)(useLetters: StringProperty => Unit): Story =
      val newStoryModel = frame.updateStory(numberModel, storyModel)
      frame.words.clear()
      val newNumberModel = storyModel.maxKey match
        case None => model.Number(1)
        case Some(n) => n.increment
      val (numberM, wordVM) = frame.addWord(wordModel)
      useLetters(wordVM.letters)
      new Story {
        override val numberModel: model.Number = newNumberModel
        override val storyModel: model.Story = newStoryModel
        override val frame: Frame = self.frame
      }

    def addWordToCurrentFrame(wordModel: model.Word): Story =
      addWordToCurrentFrameAndUseLetters(wordModel)(_ => ())

    def addWordToCurrentFrameAndUseLetters(wordModel: model.Word)(useLetters: StringProperty => Unit): Story =
      val (numberM, wordVM) = frame.addWord(wordModel)
      useLetters(wordVM.letters)
      new Story {
        override val numberModel: model.Number = self.numberModel
        override val storyModel: model.Story = self.frame.updateStory(self.numberModel, self.storyModel)
        override val frame: Frame = self.frame
      }

    def toNextFrame: Story =
      if storyModel.hasNumber(numberModel.increment) then
        val newStoryModel = frame.updateStory(numberModel, storyModel)
        frame.loadFrameFromStory(numberModel.increment, newStoryModel)
        new Story {
          override val numberModel: model.Number = self.numberModel.increment
          override val storyModel: model.Story = newStoryModel
          override val frame: Frame = self.frame
        }
      else
        if frame.nonEmpty then
          val newStoryModel = frame.appendToStory(numberModel, storyModel)
          frame.words.clear()
          new Story {
            override val numberModel: model.Number = self.numberModel.increment
            override val storyModel: model.Story = newStoryModel
            override val frame: Frame = self.frame
          }
        else
          self

    def toPreviousFrame: Story =
      if !storyModel.hasNumber(numberModel.increment) then
        new Story {
          override val numberModel: model.Number = self.numberModel
          override val storyModel: model.Story = self.frame.appendToStory(self.numberModel, self.storyModel)
          override val frame: Frame = self.frame
        }
      else
        self

      if storyModel.hasNumber(numberModel.decrement) then
        val newStoryModel = frame.updateStory(numberModel, storyModel)
        frame.loadFrameFromStory(numberModel.decrement, storyModel)
        new Story {
          override val numberModel: model.Number = self.numberModel.decrement
          override val storyModel: model.Story = newStoryModel
          override val frame: Frame = self.frame
        }
      else
        self

object Story:

  def fromModel(n: model.Number, s: model.Story, f: Frame): Story =

    new Story {
      override val numberModel: model.Number = n
      override val storyModel: model.Story = s
      override val frame: Frame = f
    }
