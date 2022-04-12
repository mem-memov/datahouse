package memmemov.datahouse

import cats.effect.kernel.Resource
import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.std.Dispatcher
import memmemov.datahouse.ScalaFXHelloWorld.stage
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene

object Application extends IOApp.Simple:
  override def run: IO[Unit] =
    for {
      app <- IO {
        new JFXApp3:
          override def start(): Unit =
            val textInput = viewModel.TextInput("")

            stage = new JFXApp3.PrimaryStage {
              title.value = "Datahouse"
              width = 600
              height = 450
              scene = new Scene { self =>
                content = view.Layout(self, textInput)
              }
            }
      }
      _ <- IO(app.main(Array.empty[String]))
    } yield ()
