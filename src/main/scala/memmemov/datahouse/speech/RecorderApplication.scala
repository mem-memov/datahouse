package memmemov.datahouse.speech

import cats.effect.{IO, IOApp}

import scala.util.{Try, Failure, Success}
import scala.concurrent.duration._

object RecorderApplication extends IOApp.Simple:
  override def run: IO[Unit] =
    val recorder = Recorder()
    for {
      starterFiber <- IO(recorder.startRecording("/tmp/voice.wav")).start
      _ <- IO.sleep(1.second) >> IO(recorder.stopRecording()) >> starterFiber.cancel
      _ <- starterFiber.join
    } yield ()