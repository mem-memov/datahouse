package memmemov.datahouse.configuration

final case class SpeechRecognition(
  token: YandexToken,
  folderId: YandexFolderId,
  yandexSpeechKitUri: YandexSpeechKitUri
)
