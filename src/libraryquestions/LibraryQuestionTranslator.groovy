package libraryquestions

import logging.Log
import translations.Translation

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionTranslator {
    /*
    Map format:
        [libraryQuestionFieldName: "BOM Fields", regex: "(.*currentAttr.*name:.*?[\"'])(.*?)([\"'].*)"],...
    */

    LibraryQuestionFieldParser libraryQuestionFieldFinder
    def libraryQuestionRegexes
    String translationKeyList

    LibraryQuestionTranslator(LibraryQuestionFieldParser libraryQuestionFieldFinder) {
        this.libraryQuestionFieldFinder = libraryQuestionFieldFinder
        this.libraryQuestionRegexes = libraryQuestionFieldFinder.libraryQuestionRegexes
    }

    def replaceTextInLibraryTextBlockWithTranslatedValues(String textBlock, Translation translation) {
        /*
        for each map in fieldRegexList
            get fieldName
            get translated value for fieldname from translation
            get text value for regex from libraryTextBlock
            if values differ, replace text in library with translated value from translation
         */
        def translatedTextBlock = textBlock
        translationKeyList = translation.translationFieldKeys.getKeyList()
        libraryQuestionRegexes.each { Map fieldsAndRegexes ->
            translatedTextBlock = translateTextForOneFieldUsingRegexMap(translatedTextBlock, translation, fieldsAndRegexes)
        }
        translatedTextBlock
    }

    def translateTextForOneFieldUsingRegexMap(String textBlock, Translation translation, Map fieldsAndRegexes) {
        def translatedTextBlock = textBlock
        def fieldName = fieldsAndRegexes.get("fieldName")
        if (fieldName.toLowerCase().contains("translated")) {
            def originalValue = libraryQuestionFieldFinder.findFieldInLibraryText(translatedTextBlock, fieldName)
            def translatedValue = translation.get(fieldName)
            if (!(originalValue.equals(translatedValue))) {
                def regex = fieldsAndRegexes.get("regex")
                def libraryTextMatcher = translatedTextBlock =~ regex
                if (libraryTextMatcher.count == 0) {
                    Log.writeLine("nocode", "No Class Factory code for: keys: $translationKeyList / $fieldName: '$translatedValue'")
                } else {
                    Log.writeLine("Keys: $translationKeyList / $fieldName: replacing '$originalValue' with '$translatedValue'")
                    def translationValue = "'" + translatedValue + "'"
                    translatedTextBlock = libraryTextMatcher[0][1] + translationValue + libraryTextMatcher[0][3]
                }
            }
        }
        translatedTextBlock
    }

}
