package libraryquestions

import logging.Log
import translations.Translation
import translations.TranslationFieldKeys
import translations.Translations

class LibraryTranslator {

    LibraryTextBlock libraryTextBlock
    Translations translations
    TranslationFieldKeys translationFieldKeys

    LibraryTranslator (LibraryTextBlock libraryTextBlock, Translations translations) {
        this.libraryTextBlock = libraryTextBlock
        this.translations = translations
    }

    def getTranslatedLibraryText() {
        translationFieldKeys = getKeyValuesFromLibraryTextBlock()
        def translation = getTranslationForKeys()
        String translatedLibraryText = applyTranslationToLibraryTextBlock(translation)
        translatedLibraryText
    }

    def getKeyValuesFromLibraryTextBlock() {
        def bomFieldName = libraryTextBlock.findFieldInLibraryText("BOM Fields")
        def questionIdentifier = libraryTextBlock.findFieldInLibraryText("Question Identifier")
        def translationFieldKeys = null
        if ((bomFieldName != null) || (questionIdentifier != null))
            translationFieldKeys = new TranslationFieldKeys(["BOM Fields": bomFieldName, "Question Identifier": questionIdentifier])
        translationFieldKeys
    }

    def getTranslationForKeys() {
        /*
      get translations for multiple keys...if not exactly one match, return null
       */
        Translation matchingTranslationFromExcelExport = null
        if (translationFieldKeys != null) {
            def matchingTranslations = translations.getTranslationsFromKeyFields(translationFieldKeys)
            if (isSingleMatchingTranslationForKeys(matchingTranslations)) {
                matchingTranslationFromExcelExport = matchingTranslations[0]
                matchingTranslationFromExcelExport.translationFieldKeys = translationFieldKeys  // used for missing value messages
            }
        }
        matchingTranslationFromExcelExport
    }

    def isSingleMatchingTranslationForKeys(matchingTranslations) {
        def translationCount = matchingTranslations.size()
        if (translationCount == 1)
            return true
        else if (translationCount == 0) {
            Log.writeLine "exceptions", "Missing translation for keys: ${translationFieldKeys.getKeyList()}"
            return false
        } else if (translationCount > 1) {
            Log.writeLine "exceptions", "Multiple translations for keys: ${translationFieldKeys.getKeyList()}"
            return false
        }
    }

    def applyTranslationToLibraryTextBlock(Translation translation) {
        String translatedLibraryText = libraryTextBlock.textBlock
        if (translation != null)
            translatedLibraryText = libraryTextBlock.translateAllFieldsFromTranslation(translation)
        translatedLibraryText
    }


}
