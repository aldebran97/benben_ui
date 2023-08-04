package com.example.benben_front

/**
 * 常量定义
 * @author aldebran
 * @since 2023-08-04
 */
class Constants {

    companion object {
        val LIB_NAME_CONFIG_MAP = mutableMapOf<String, SearchConfig>().apply {
            this["poems"] = SearchConfig().apply {
                this.topK = 2
                this.threshold = 0.4
            }

            this["wiki_interesting_lib"] = SearchConfig().apply {
                this.topK = 10
                this.threshold = 0.4
            }
        }
    }
}


class SearchConfig {
    var topK = 10
    var threshold = 0.5
}

val Constants.Companion.TEMP_FOLDER
    get() = "D:\\user_dir\\temp\\benben"

val Constants.Companion.DOCKER_FOLDER
    get() = "/root/share/temp/benben"

val Constants.Companion.CHAT_SERVICE_IP
    get() = "localhost"

val Constants.Companion.CHAT_SERVICE_NOT_STREAM_PORT
    get() = 10002

val Constants.Companion.CHAT_SERVICE_STREAM_PORT
    get() = 10001

val Constants.Companion.CHAT_SERVICE_NOT_STREAM_URL
    get() = "http://$CHAT_SERVICE_IP:$CHAT_SERVICE_NOT_STREAM_PORT/chat"

val Constants.Companion.TTS_IP
    get() = "127.0.0.1"

val Constants.Companion.TTS_PORT
    get() = 10003

val Constants.Companion.TTS_URL
    get() = "http://$TTS_IP:$TTS_PORT/text_to_audio"


val Constants.Companion.APTV_IP
    get() = "127.0.0.1"

val Constants.Companion.APTV_PORT
    get() = 9002

val Constants.Companion.APTV_URL
    get() = "http://$APTV_IP:$APTV_PORT/audio_picture_to_video"

val Constants.Companion.SIMILARITY_SEARCH_IP
    get() = "127.0.0.1"

val Constants.Companion.SIMILARITY_SEARCH_PORT
    get() = 10004

val Constants.Companion.SIMILARITY_SEARCH_URL
    get() = "http://$SIMILARITY_SEARCH_IP:$SIMILARITY_SEARCH_PORT/lib/similaritySearch"

val Constants.Companion.PROMPT_MAX_LENGTH
    get() = 2000

