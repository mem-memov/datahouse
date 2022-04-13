package memmemov.datahouse.speech

sealed trait ButtonMessage
case class StartButtonMessage(filePath: String) extends ButtonMessage
case class StopButtonMessage() extends ButtonMessage
