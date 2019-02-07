package libraryquestions

import translations.Translation

class LibraryTextBlock {
    String textBlock
    LibraryQuestionFieldParser libraryQuestionFieldFinder
    LibraryQuestionTranslator libraryQuestionTranslator

    LibraryTextBlock(textBlock, libraryQuestionFieldFinder) {
        this.textBlock = textBlock
        this.libraryQuestionFieldFinder = libraryQuestionFieldFinder
        this.libraryQuestionTranslator = new LibraryQuestionTranslator(libraryQuestionFieldFinder)
    }

    def findFieldInLibraryText(String searchText) {
        libraryQuestionFieldFinder.findFieldInLibraryText(this.textBlock, searchText)
    }

    def translateAllFieldsFromTranslation(Translation translation) {
        String translatedTextBlock = libraryQuestionTranslator.replaceTextInLibraryTextBlockWithTranslatedValues(textBlock, translation)
        translatedTextBlock
    }
}
