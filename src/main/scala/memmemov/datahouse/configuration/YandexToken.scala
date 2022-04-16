package memmemov.datahouse.configuration

import cats.Show

case class YandexToken(value: String):

  given Show[YandexToken] with
    override def show(t: YandexToken): String = t.value

