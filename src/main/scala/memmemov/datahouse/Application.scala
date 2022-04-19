package memmemov.datahouse

import cats.effect.{ExitCode, Fiber, IO, IOApp, Ref}
import cats.effect.std.{Dispatcher, Queue}
import fs2.Stream
import javafx.application.Platform
import memmemov.datahouse.configuration.Loader
import memmemov.datahouse.speech.{ButtonMessage, RecognitionFailed, Recorder, StartButtonMessage, StopButtonMessage, VoiceRecognized, YandexClient, YandexResponse}
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*

object Application extends IOApp:

  override def run(args: List[String]): IO[ExitCode] =

    val storageDirectory = args.head // TODO validate folder

    val recorder: Recorder = Recorder() // TODO use recorder as a Resource

    Dispatcher[IO].use { dispatcher =>
      for {
        config <- Loader.load[IO](storageDirectory)
        recorderQueue <- Queue.unbounded[IO, Option[ButtonMessage]]
        recorderStream <- IO(Stream.fromQueueNoneTerminated(recorderQueue))
        - <- handleRecorderButtons(recorderStream, recorder, config.speechRecognition)
        _ <- IO {UserInterface(dispatcher, recorderQueue, config.storageDirectory).main(Array.empty[String])}
      } yield ExitCode.Success
    }

  private def handleRecorderButtons(recorderStream: Stream[IO, ButtonMessage], recorder: Recorder, config: configuration.SpeechRecognition) =
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
                  recordingFiber.cancel
            }
            resultText <- YandexClient.putRequest(config.folderId, config.token, recorder.recordedFilePath)
            _ <- IO {
              decode[YandexResponse](resultText) match
                case Left(_) => letters.set("aй")
                case Right(VoiceRecognized(result)) => letters.set(result)
                case Right(RecognitionFailed(errorCode, errorMessage)) => letters.set("ой")
            }
          } yield ()
      }.compile.drain.start
    } yield ()

