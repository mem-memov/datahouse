package memmemov.datahouse

import cats.effect.{IO, IOApp}
import cats.effect.std.{Dispatcher, Queue}
import fs2.Stream
import javafx.application.Platform
import memmemov.datahouse.speech.{ButtonMessage, Recorder, StartButtonMessage, StopButtonMessage}

object Application extends IOApp.Simple:
  override def run: IO[Unit] =
    val recorder: Recorder = Recorder()

    Dispatcher[IO].use { dispatcher =>
      for {
        recorderQueue <- Queue.unbounded[IO, Option[ButtonMessage]]
        recorderStream <- IO(Stream.fromQueueNoneTerminated(recorderQueue))
        _ <- recorderStream.evalMap{
          case StartButtonMessage(filePath) =>
            for {
              _ <- IO(println("start"))
              recordingFiber <- IO(recorder.startRecording(filePath)).start // TODO cancel fiber on stop button
            } yield ()
          case StopButtonMessage() => IO(println("stop")) >> IO(recorder.stopRecording())
        }.compile.drain.start
        _ <- IO {UserInterface(dispatcher, recorderQueue).main(Array.empty[String])}
      } yield ()
    }

