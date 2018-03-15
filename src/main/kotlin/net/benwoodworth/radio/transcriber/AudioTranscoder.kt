package net.benwoodworth.radio.transcriber

import java.io.InputStream
import java.io.OutputStream

interface AudioTranscoder {

    fun transcode(input: InputStream): InputStream

}