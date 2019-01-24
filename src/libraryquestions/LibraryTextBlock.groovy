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
}
