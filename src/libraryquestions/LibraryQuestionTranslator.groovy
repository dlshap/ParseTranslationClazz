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

    LibraryQuestionFieldParser libraryQuestionFieldParser
    def libraryQuestionRegexes
    String translationKeyList

    LibraryQuestionTranslator(LibraryQuestionFieldParser libraryQuestionFieldParser) {
        this.libraryQuestionFieldParser = libraryQuestionFieldParser
        this.libraryQuestionRegexes = libraryQuestionFieldParser.libraryQuestionRegexes
    }

    def replaceTextInLibraryTextBlockWithTranslatedValues(String textBlock, Translation translation) {
        def translatedTextBlockForLanguage = LibraryLanguageAdder.addLanguage(textBlock, libraryQuestionFieldParser.languageName)
        translatedTextBlockForLanguage = translateEachTextBlockFieldFromTranslations(translatedTextBlockForLanguage, translation)
        translatedTextBlockForLanguage
        }

    def translateEachTextBlockFieldFromTranslations(String textBlock, Translation translation) {
        /*
        for each map in fieldRegexList
            get fieldName
            get translated value for fieldname from translation
            get text value for regex from libraryTextBlock
            if values differ, replace text in library with translated value from translation
         */
        def translatedTextBlock = textBlock
        translationKeyList = translation.translationFieldKeys.getKeyList()      // for error logging
        libraryQuestionRegexes.each { Map fieldsAndRegexes ->
            translatedTextBlock = translateTextForOneFieldUsingRegexMap(translatedTextBlock, translation, fieldsAndRegexes)
        }
        translatedTextBlock
    }

    def translateTextForOneFieldUsingRegexMap(String textBlock, Translation translation, Map fieldsAndRegexes) {
        def fieldName = fieldsAndRegexes.get("fieldName")
        def translatedTextBlock = textBlock
        if (fieldName.toLowerCase().contains("translated")) {
            def originalValue = libraryQuestionFieldParser.findFieldInLibraryText(textBlock, fieldName)
            def translatedValue = translation.get(fieldName)
            if (!(originalValue.equals(translatedValue)) && (translatedValue != null)) {
                def regex = fieldsAndRegexes.get("regex")
                def libraryTextMatcher = translatedTextBlock =~ regex
                if (libraryTextMatcher.count == 0) {
                    Log.writeLine("nocode", "No Class Factory code for: keys: $translationKeyList / $fieldName: '$translatedValue'")
                } else {
                    Log.writeLine("Keys: $translationKeyList / $fieldName: replacing '$originalValue' with '$translatedValue'")
                    def translationValue = "'" + translatedValue + "'"
//                    for testing:
//                    def match1 = libraryTextMatcher[0][1]
//                    def match2 = libraryTextMatcher[0][2]
//                    def match3 = libraryTextMatcher[0][3]
                    translatedTextBlock = libraryTextMatcher[0][1] + translationValue + libraryTextMatcher[0][3]
                }
            }
        }
        translatedTextBlock
    }
}
