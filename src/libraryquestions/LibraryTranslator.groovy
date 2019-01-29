package libraryquestions

import logging.Log
import translations.Translation
import translations.TranslationFieldKeys
import translations.Translations

class LibraryTranslator {

    LibraryTextBlock libraryTextBlock
    Translations translations

    LibraryTranslator (LibraryTextBlock libraryTextBlock, Translations translations) {
        this.libraryTextBlock = libraryTextBlock
        this.translations = translations
    }

    def getTranslatedText() {
        def translatedText = libraryTextBlock.textBlock
        //todo: translate it and generate string
        translatedText
    }

//    def getTranslatedTextForNextLibraryTextBlockUsingExcelTranslations(LibraryTextBlock libraryTextBlock, Translations translationsFromExcelExport) {
//        TranslationFieldKeys factoryTranslationKeys = getKeyValuesFromLibraryTextBlock(libraryTextBlock)
//        Translation translation = getTranslationForKeys(translationsFromExcelExport, factoryTranslationKeys)
//        String translatedLibraryText = applyTranslationToLibraryTextBlock(translation, libraryTextBlock)
//        translatedLibraryText
//    }
//
//    def getKeyValuesFromLibraryTextBlock(LibraryTextBlock libraryTextBlock) {
//        def bomFieldName = libraryTextBlock.findFieldInLibraryText("BOM Fields")
//        def questionIdentifier = libraryTextBlock.findFieldInLibraryText("Question Identifier")
//        def translationFieldKeys = null
//        if ((bomFieldName != null) || (questionIdentifier != null))
//            translationFieldKeys = new TranslationFieldKeys(["BOM Fields": bomFieldName, "Question Identifier": questionIdentifier])
//        translationFieldKeys
//    }
//
//    def getTranslationForKeys(Translations translationsFromExcelExport, TranslationFieldKeys translationFieldKeys) {
//        /*
//      get translations for multiple keys...if not exactly one match, return null
//       */
//        Translation matchingTranslationFromExcelExport = null
//        if (translationFieldKeys != null) {
//            def matchingTranslations = translationsFromExcelExport.getTranslationsFromKeyFields(translationFieldKeys)
//            if (isSingleMatchingTranslationForKeys(matchingTranslations, translationFieldKeys)) {
//                matchingTranslationFromExcelExport = matchingTranslations[0]
//                matchingTranslationFromExcelExport.translationFieldKeys = translationFieldKeys  // used for missing value messages
//            }
//        }
//        matchingTranslationFromExcelExport
//    }
//
//    def isSingleMatchingTranslationForKeys(matchingTranslations, TranslationFieldKeys translationFieldKeys) {
//        def translationCount = matchingTranslations.size()
//        if (translationCount == 1)
//            return true
//        else if (translationCount == 0) {
//            Log.writeLine "exceptions", "Missing translation for keys: ${translationFieldKeys.getKeyList()}"
//            return false
//        } else if (translationCount > 1) {
//            Log.writeLine "exceptions", "Multiple translations for keys: ${translationFieldKeys.getKeyList()}"
//            return false
//        }
//    }
//
//    def applyTranslationToLibraryTextBlock(Translation translation, LibraryTextBlock libraryTextBlock) {
//        String translatedLibraryText = libraryTextBlock.textBlock
//        if (translation != null)
//            translatedLibraryText = libraryTextBlock.translateAllFieldsFromTranslation(translation)
//        translatedLibraryText
//    }


}
