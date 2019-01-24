package libraryquestions

import logging.Log
import translations.Translation

class LibraryTextBlock {
    String textBlock
    LibraryQuestionFieldFinder libraryQuestionFieldFinder

    LibraryTextBlock(textBlock, libraryQuestionFieldFinder) {
        this.textBlock = textBlock
        this.libraryQuestionFieldFinder = libraryQuestionFieldFinder
    }

    def lineContains(String searchText) {
        libraryQuestionFieldFinder.lineContains(this.textBlock, searchText)
    }

    def findFieldInLibraryText(String searchText) {
        libraryQuestionFieldFinder.findFieldInLibraryText(this.textBlock, searchText)
    }

    def translateAllFieldsFromTranslation(Translation translation) {
        String translatedTextBlock = replaceTextInLibraryTextBlockWithTranslatedValues(translation)
        translatedTextBlock
    }

    def replaceTextInLibraryTextBlockWithTranslatedValues(Translation translation) {
        /*
        for each map in fieldRegexList
            get fieldName
            get translated value for fieldname from translation
            get text value for regex from libraryTextBlock
            if values differ, replace text in library with translated value from translation
         */
        def translatedTextBlock = textBlock
        def libraryQuestionRegexes = libraryQuestionFieldFinder.libraryQuestionRegexes
        String translationKeyList = translation.translationFieldKeys.getKeyList()
        libraryQuestionRegexes.eachWithIndex { Map regexMap, int i ->
            def fieldName = regexMap.get("fieldName")
            if (fieldName.toLowerCase().contains("translated")) {
                def originalValue = libraryQuestionFieldFinder.findFieldInLibraryText(textBlock, fieldName)
                def translatedValue = translation.get(fieldName)
                if (!(originalValue.equals(translatedValue))) {
                    def regex = regexMap.get("regex")
                    def libraryTextMatcher = translatedTextBlock =~ regex
                    if (libraryTextMatcher.count == 0) {
                        Log.writeLine("nocode", "No Class Factory code for: keys: $translationKeyList / $fieldName: '$translatedValue'")
                    } else {
                        Log.writeLine("Keys: $translationKeyList / $fieldName: replacing '$originalValue' with '$translatedValue'")
                        def translationValue = "'" + translatedValue + "'"
                        def before = libraryTextMatcher[0][1]
                        def after = libraryTextMatcher[0][3]
                        translatedTextBlock = libraryTextMatcher[0][1] + translationValue + libraryTextMatcher[0][3]
                    }
                }
            }
        }
        translatedTextBlock
    }

//    def result = nextText =~ regex
//        if (!(libraryValue.equals(translationValue))) {
//            Log.writeLine("keys: ${translationFieldKeys.getKeyList()} / $translationFieldName: replacing '$libraryValue' with '$translationValue'")
//            translationValue = "'" + translationValue + "'"
//            translatedText = result[0][1] + translationValue + result[0][3]

    //  static replaceLineWithTranslations() {
//        if ((translationFieldKeys != null) && (matchingTranslationFromExcelExport != null)) {
//            def tryToTranslateFactoryTextBlock = nextFactoryTextBlock
//            def libraryQuestionTranslators = libraryQuestionFieldFinder.getLibraryQuestionTranslators()
//            libraryQuestionTranslators.eachWithIndex { LibraryQuestionTranslator it, i ->
//                // get field name from translator
//                def translationKey = it.getValue("excelExportFieldName")
//                if (translationKey.toLowerCase().contains("translated")) {
//                    // get translation value from translation (keyfile)
//                    def translationValue = matchingTranslationFromExcelExport.get(translationKey)
//                    // translate it if there is a match...leave alone if not
//                    if (translationValue != "") {
//                        tryToTranslateFactoryTextBlock = it.translate(tryToTranslateFactoryTextBlock, languageName, translationValue, translationFieldKeys)
//                    }
//                }
//            }
//            nextFactoryTextBlock = tryToTranslateFactoryTextBlock
//        }
//    }
}
