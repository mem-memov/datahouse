package memmemov.datahouse.speech

import scala.util.Try
import javax.sound.sampled.{AudioFormat, AudioFileFormat, DataLine, TargetDataLine, AudioSystem, AudioInputStream}
import java.io._

class Recorder(
  private val line: TargetDataLine,
  private val format: AudioFormat
):
  def startRecording(filePath: String): Unit =
    val file = new File(filePath)
    val fileType: AudioFileFormat.Type = AudioFileFormat.Type.WAVE
    line.open(format)
    line.start()
    val ais = new AudioInputStream(line)
    AudioSystem.write(ais, fileType, file)

  def stopRecording(): Unit =
    line.stop()
    line.close()



object Recorder:

  def apply(): Recorder =
      val format = getAudioFormat
      val info = new DataLine.Info(classOf[TargetDataLine], format)
      if (!AudioSystem.isLineSupported(info)) {
        println("Line not supported")
        throw new RuntimeException("Data line not supported")
      }
      val line = AudioSystem.getLine(info).asInstanceOf[TargetDataLine]
      new Recorder(line, format)

  private def getAudioFormat: AudioFormat =
    val sampleRate = 16000
    val sampleSizeInBits = 8
    val channels = 2
    val signed = true
    val bigEndian = true
    new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian)

