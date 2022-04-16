package memmemov.datahouse.configuration

import cats.effect.Async
import ciris.{ConfigValue, env}
import ciris.refined.*
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import cats.implicits._

object Loader:
  def load[F[_] : Async]: F[Application] =
    default[F](
      YandexSpeechKitUri("https://stt.api.cloud.yandex.net/speech/v1/stt:recognize")
    ).load[F]

  private def default[F[_]](
    yandexSpeechKitUri: YandexSpeechKitUri
  ): ConfigValue[F, Application] =
    (
      env("YANDEX_TOKEN").as[NonEmptyString].map(YandexToken(_)),
      env("YANDEX_FOLDER_ID").as[NonEmptyString].map(YandexFolderId(_))
    ).parMapN {
      (yandexToken, yandexFolderId) =>
        Application(
          SpeechRecognition(yandexToken, yandexFolderId, yandexSpeechKitUri)
        )
    }

