package libraryquestions

import logging.Log
import translations.Translation

class LibraryTextBlock {
    String textBlock
    LibraryQuestionFieldFinder libraryQuestionFieldFinder
    LibraryQuestionTranslator libraryQuestionTranslator

    LibraryTextBlock(textBlock, libraryQuestionFieldFinder) {
        this.textBlock = textBlock
        this.libraryQuestionFieldFinder = libraryQuestionFieldFinder
        this.libraryQuestionTranslator = new LibraryQuestionTranslator(libraryQuestionFieldFinder)
    }

    def lineContains(String searchText) {
        libraryQuestionFieldFinder.lineContains(this.textBlock, searchText)
    }

    def findFieldInLibraryText(String searchText) {
        libraryQuestionFieldFinder.findFieldInLibraryText(this.textBlock, searchText)
    }

    def translateAllFieldsFromTranslation(Translation translation) {
        String translatedTextBlock = libraryQuestionTranslator.replaceTextInLibraryTextBlockWithTranslatedValues(textBlock, translation)
        translatedTextBlock
    }
}
