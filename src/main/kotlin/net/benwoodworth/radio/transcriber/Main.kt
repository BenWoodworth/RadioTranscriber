package net.benwoodworth.radio.transcriber

import edu.cmu.sphinx.api.Configuration
import edu.cmu.sphinx.api.SpeechResult
import edu.cmu.sphinx.api.StreamSpeechRecognizer
import org.apache.commons.io.IOUtils
import java.io.File
import java.net.URL


fun main(args: Array<String>) {
    val configuration = Configuration().apply {
        acousticModelPath = "resource:/edu/cmu/sphinx/models/en-us/en-us"
        dictionaryPath = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict"
        languageModelPath = "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin"

    }
    val recognizer = StreamSpeechRecognizer(configuration)


    val streamUrl = URL("http://54.196.113.103/entercom-wbzafmaac-64")
    val connection = streamUrl.openConnection()
    val radioStream = connection.getInputStream()

    val transcoder = AudioTranscoderPcmS16Le()
    val transcodedStream = transcoder.transcode(radioStream)


    recognizer.startRecognition(transcodedStream)
    var result: SpeechResult
    while (true) {
        result = recognizer.result ?: break
        println("Hypothesis: ${result.hypothesis}")
    }

    recognizer.stopRecognition()
}