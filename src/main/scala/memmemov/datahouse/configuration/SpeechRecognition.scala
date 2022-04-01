package memmemov.datahouse.configuration

final case class FolderId(val value: String) extends AnyVal
final case class Token(val value: String) extends AnyVal
final case class BaseUrl(val value: String) extends AnyVal

final case class SpeechRecognition(folderId: FolderId, token: Token, baseUrl: BaseUrl)
