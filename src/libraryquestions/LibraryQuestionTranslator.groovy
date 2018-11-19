package libraryquestions

import logging.Log

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionTranslator {
    /*
    Map format:
        [transKeyField: "BOM Fields", regex: "(.*currentAttr.*name:.*?[\"'])(.*?)([\"'].*)"],...
    */
    def libQuestionMap = [:]

    def LibraryQuestionTranslator(libQMap) {
        this.libQuestionMap = libQMap
    }

    def getValue(keyname) {
        libQuestionMap[keyname]
    }

    def translate(nextText, translationValue, bomFieldName) {
        def translatedText = nextText
        if (translationValue != null) {
            def libraryValue = LibraryQuestionMatchers.getFactoryMatchingValue(nextText, this.getValue("transKeyField"))
            def regex = this.getValue("regex")
            def translationFieldName = this.getValue("transKeyField")
            def result = nextText =~ regex
            if (result.count == 0) {
                Log.writeLine("nocode", "No Class Factory code for: $bomFieldName/$translationFieldName: '$translationValue'")
            } else {
                if (!libraryValue.equals(translationValue)) {
                    Log.writeLine("$bomFieldName/$translationFieldName: replacing '$libraryValue' with '$translationValue'")
                    translationValue = "'" + translationValue + "'"
                    translatedText = result[0][1] + translationValue + result[0][3]
                }
            }
        }
        translatedText
    }
}