package LibraryQuestions

import Logging.Log

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionTranslator {
    /*
    Map format:
        [transKeyField: "BOM Fields", regex: "(.*currentAttr.*name:.*?[\"'])(.*?)([\"'].*)"],...
    */
    def libQMap = [:]

    def LibraryQuestionTranslator(libQMap) {
        this.libQMap = libQMap
    }

    def getValue(keyname) {
        libQMap[keyname]
    }

    def translate(nextText, translationValue, bomFieldName) {
        def translatedText = nextText
        def regex = this.getValue("regex")
        def fieldName = this.getValue("transKeyField")
        def result = nextText =~ regex
        if (result.count != 0) {
            if (!result[0][2].trim().equals(translationValue.trim())) {
                Log.writeLine("$bomFieldName/$fieldName: replacing '${result[0][2]}' with '$translationValue'")
                translatedText = result[0][1] + translationValue + result[0][3]
            }
        }
        translatedText
    }
}