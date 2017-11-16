package Translations

import groovy.json.StringEscapeUtils

class UpdateRMTProperties {

    static tryThis() {
        def data = "Hello: がチェックアウトかロックしているため、編集できません。後で編集してください。"
        def result = ""

        data.each { ch ->
            result += "\\u"
            int c = (int) ch
            def str = Integer.toHexString(c)
            if (c < 255)
                result += "00"
            result += str
        }

        println result
        println StringEscapeUtils.unescapeJava(result)
    }


    static main(args) {
        tryThis()
    }
}
