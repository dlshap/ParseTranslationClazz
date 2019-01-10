package libraryquestions

import logging.Log
import translations.TranslationFieldKeys

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionTranslator {
    /*
    Map format:
        [libraryQuestionFieldName: "BOM Fields", regex: "(.*currentAttr.*name:.*?[\"'])(.*?)([\"'].*)"],...
    */
    def libraryQuestionMap = [:]

    def LibraryQuestionTranslator(libraryQuestionMap) {
        this.libraryQuestionMap = libraryQuestionMap
    }

    def getValue(keyname) {
        libraryQuestionMap[keyname]
    }

    def translate(nextText, languageName, translationValue, TranslationFieldKeys translationFieldKeys) {
        def translatedText = nextText
        if (translationValue != null) {
            def libraryValue = new LibraryQuestionFieldFinder(languageName).findFieldInLibraryText(nextText, languageName, this.getValue("excelExportFieldName"))
            def regex = this.getValue("regex")
            def translationFieldName = this.getValue("excelExportFieldName")
            def result = nextText =~ regex
            if (result.count == 0) {
                Log.writeLine("nocode", "No Class Factory code for: keys: ${translationFieldKeys.getKeyList()} / $translationFieldName: '$translationValue'")
            } else {
                if (!(libraryValue.equals(translationValue))) {
                    Log.writeLine("keys: ${translationFieldKeys.getKeyList()} / $translationFieldName: replacing '$libraryValue' with '$translationValue'")
                    translationValue = "'" + translationValue + "'"
                    translatedText = result[0][1] + translationValue + result[0][3]
                }
            }
        }
        translatedText
    }
}
