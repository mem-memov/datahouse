package memmemov.datahouse.speech

import scalafx.beans.property.StringProperty

sealed trait ButtonMessage
case class StartButtonMessage(filePath: String) extends ButtonMessage
case class StopButtonMessage(letters: StringProperty) extends ButtonMessage
