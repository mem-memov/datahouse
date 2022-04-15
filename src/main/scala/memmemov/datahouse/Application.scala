package memmemov.datahouse

import cats.effect.{Fiber, IO, IOApp, Ref}
import cats.effect.std.{Dispatcher, Queue}
import fs2.Stream
import javafx.application.Platform
import memmemov.datahouse.speech.{ButtonMessage, Recorder, StartButtonMessage, StopButtonMessage}

object Application extends IOApp.Simple:
  override def run: IO[Unit] =
    val recorder: Recorder = Recorder() // TODO use recorder as a Resource

    Dispatcher[IO].use { dispatcher =>
      for {
        recorderQueue <- Queue.unbounded[IO, Option[ButtonMessage]]
        recorderStream <- IO(Stream.fromQueueNoneTerminated(recorderQueue))
        - <- handleRecorderButtons(recorderStream, recorder)
        _ <- IO {UserInterface(dispatcher, recorderQueue).main(Array.empty[String])}
      } yield ()
    }

  private def handleRecorderButtons(recorderStream: Stream[IO, ButtonMessage], recorder: Recorder) =
    for {
      optionalRecordingFiberRef: Ref[IO, Option[Fiber[IO, Throwable, Unit]]] <- Ref[IO].of(Option.empty[Fiber[IO, Throwable, Unit]])
      _ <- recorderStream.evalMap{
        case StartButtonMessage(filePath) =>
          for {
            recordingFiber <- IO(recorder.startRecording(filePath)).start
            _ <- optionalRecordingFiberRef.update {
              case None =>
                Some(recordingFiber)
              case Some(previousRecordingFiber) =>
                previousRecordingFiber.cancel
                Some(recordingFiber)
            }
          } yield ()
        case StopButtonMessage(letters) =>
          for {
            optionalRecordingFiber <- optionalRecordingFiberRef.get
            _ <- IO {
              optionalRecordingFiber match
                case None => ()
                case Some(recordingFiber) =>
                  recorder.stopRecording()
                  letters.set("Яндекс")
                  recordingFiber.cancel
            }
          } yield ()
      }.compile.drain.start
    } yield ()

