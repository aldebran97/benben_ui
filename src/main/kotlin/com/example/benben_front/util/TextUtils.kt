package com.example.benben_front.util

/**
 * 文本处理工具
 * @author aldebran
 * @since 2023-08-04
 */
class TextUtils {

    companion object {

        var meBlockTemplate = """
            |<div>
            |<p class="p1">我：</p>
            |<p>CONTENT</p>
            |</div>
            |<br/>
""".trimMargin()

        var benbenBlockTemplate = """
            |<div>
            |<p class="p1">犇犇：</p>
            |<p>CONTENT</p>
            |</div>
            |<br/>
""".trimMargin()

        fun historyToHtml(history: List<List<String>>): String {
            val sb = StringBuilder()
            for (list in history) {
                var question = list[0].replace("\n", "<br/>")
                var answer = list[1].replace("\n", "<br/>").replace("🐂", "")
                sb.append(meBlockTemplate.replace("CONTENT", question))
                sb.append(benbenBlockTemplate.replace("CONTENT", answer))
            }
            return sb.toString()
        }
    }


}