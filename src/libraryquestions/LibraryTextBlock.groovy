package libraryquestions

import com.sun.jna.Library
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
        String translatedTextBlock = textBlock
        translatedTextBlock
    }

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
