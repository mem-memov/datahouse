package memmemov.datahouse.speech

import cats.syntax.functor._
import io.circe.{ Decoder, Encoder }, io.circe.generic.auto._
import io.circe.syntax._

sealed trait YandexResponse
case class VoiceRecognized(result: String) extends YandexResponse
case class RecognitionFailed(errorCode: String, errorMessage: String) extends YandexResponse

object YandexResponse:
  given Encoder[YandexResponse] = Encoder.instance {
    case voiceRecognized @ VoiceRecognized(_) => voiceRecognized.asJson
    case recognitionFailed @ RecognitionFailed(_, _) => recognitionFailed.asJson
  }

  given Decoder[YandexResponse] =
    List[Decoder[YandexResponse]](
      Decoder[VoiceRecognized].widen,
      Decoder[RecognitionFailed].widen
    ).reduceLeft(_ or _)