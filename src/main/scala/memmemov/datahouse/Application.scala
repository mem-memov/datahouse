package memmemov.datahouse

import cats.effect.{IO, IOApp}
import cats.effect.std.Dispatcher
import javafx.application.Platform

object Application extends IOApp.Simple:
  override def run: IO[Unit] =
    Dispatcher[IO].use { dispatcher =>
      for {
        _ <- IO {UserInterface(dispatcher).main(Array.empty[String])}
      } yield ()
    }

