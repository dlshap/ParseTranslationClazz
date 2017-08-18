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

    def translate(aLine, translationValue, bomFieldName) {
        def regex = this.getValue("regex")
        def fieldName = this.getValue("transKeyField")
        def result = aLine =~ regex
        if (result.count ==0)
            aLine
        else {
            if (!result[0][2].trim().length().equals(translationValue.trim().length())) {
                Log.writeLine("$bomFieldName/$fieldName: replacing '${result[0][2]}' with '$translationValue'")
            }
            result[0][1] + translationValue + result[0][3]
        }
    }
}
