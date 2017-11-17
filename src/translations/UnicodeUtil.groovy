package translations

import groovy.json.StringEscapeUtils

class UnicodeUtil {
    static unicodeEncode(String textString) {
        String result = ""
        textString.each { ch ->
            result += "\\u"
            int c = (int) ch
            def uniString = (Integer.toHexString(c)).padLeft(4, "0")
            result += uniString
        }
        result
    }

    static unicodeDecode(String unicodeString) {
        StringEscapeUtils.unescapeJava(unicodeString)
    }

    static encodeSpaces(String textString) {
        textString.replaceAll(/ /,"\\\\ ")
    }
}
