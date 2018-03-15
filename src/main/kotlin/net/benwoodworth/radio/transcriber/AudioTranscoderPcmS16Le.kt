package net.benwoodworth.radio.transcriber

import org.apache.commons.io.IOUtils
import java.io.*

/**
 * Transcodes an audio stream into a format usable by CMU Sphinx.
 */
class AudioTranscoderPcmS16Le(
        bitrate: Int = 16000
) : AudioTranscoder {

    /*
    RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, mono 16000 Hz
    RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, mono 8000 Hz

    ffmpeg -f wav -acodec pcm_s16le -ar 16000 -ac 1 pipe:
    ffmpeg -i http://54.196.113.103/entercom-wbzafmaac-64 -f wav -acodec pcm_s16le -ar 16000 -ac 1 outtt

    ffmpeg -hide_banner -f wav -acodec pcm_s16le -ar 16000 -ac 1 pipe:
    */

    private val ffmpegCommand = arrayOf(
            "ffmpeg",
            "-hide_banner", // Hide banner
            "-i", "pipe:", // Pipe input
            "-f", "wav", // Output format
            "-acodec", "pcm_s16le", // Output codec
            "-ar", "16000", // Output bitrate (16000/8000)
            "-ac", "1", // Output channels
            "pipe:" // Pipe output
    )

    override fun transcode(input: InputStream): InputStream {
        val process = ProcessBuilder()
                .command(*ffmpegCommand)
                //.redirectErrorStream(true)
                .start()

        val pipe = Thread {
            IOUtils.copy(input, process.outputStream)
        }

        pipe.start()
        return process.inputStream
    }
}