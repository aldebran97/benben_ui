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
            sb.append("""
                <script>
                    function scrollToBottom() {
                        window.scrollTo(0, document.body.scrollHeight);
                    }
                    history.scrollRestoration = "manual";
                    window.onload = scrollToBottom;
                </script>
            """.trimIndent())
            sb.append("""
                <div class="wrapper">
            """.trimIndent())
            for (list in history) {
                var question = list[0].replace("\n", "<br/>")
                var answer = list[1].replace("\n", "<br/>").replace("🐂", "")
                sb.append(meBlockTemplate.replace("CONTENT", question))
                sb.append(benbenBlockTemplate.replace("CONTENT", answer))
            }
            sb.append("""</div>""")
            return sb.toString()
        }

        fun lastIndexOf(strings: Collection<String>, input: String): Int {
            return mutableListOf<Int>().apply {
                strings.forEach {
                    this.add(input.lastIndexOf(it))
                }
            }.max()
        }

    }


}