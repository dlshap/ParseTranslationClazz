package libraryquestions

import com.sun.jna.Library

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
}
