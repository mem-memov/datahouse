package memmemov.datahouse.configuration

import ciris._
import cats.effect.{IO, IOApp}


final case class Application(speechRecognition: SpeechRecognition)

object Application {
//  val configuration: IO[ConfigValue[IO, Application]] = new Path("config.local.yaml").map(file)
}

object Test extends IOApp.Simple {
  import Application._

  override def run: IO[Unit] = IO.unit
}