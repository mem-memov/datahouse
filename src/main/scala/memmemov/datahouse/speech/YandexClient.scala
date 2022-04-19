package memmemov.datahouse.speech

import cats.effect.{IO, IOApp}
import org.http4s.ember.client.*
import org.http4s.client.*
import org.http4s.client.dsl.io.*
import org.http4s.headers.*
import org.http4s.{Entity, Header, Headers, HttpVersion, MediaType, Method, Request, Uri}
import fs2.Stream
import fs2.io.file.{Files, Path}
import memmemov.datahouse.configuration.{YandexFolderId, YandexToken}

object YandexClient {
  def putRequest(folderId: YandexFolderId, token: YandexToken, filePath: String) =

    val request: Request[IO] = Request[IO](
      Method.POST,
      Uri.unsafeFromString(s"https://stt.api.cloud.yandex.net/speech/v1/stt:recognize?folderId=${folderId.value}&format=lpcm&sampleRateHertz=16000"),
      HttpVersion.`HTTP/1.1`,
      Headers(List(
        ("Accept" -> "application/json"),
        ("Accept-Charset" -> "utf-8"),
        ("Authorization" -> s"Bearer ${token.value}")
      )),
      Entity[IO](Files[IO].readAll(Path(filePath)))
    )

    EmberClientBuilder.default[IO].build.use { client =>
      client.expect[String](request)
    }
}

//object ClientApp extends IOApp.Simple:
//
//  override def run: IO[Unit] =
//    for {
//      res <- YandexClient.putRequest()
//      _ <- IO(println(res))
//    } yield ()
