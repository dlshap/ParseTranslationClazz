package libraryquestions

import filemanagement.TextFile
import com.google.common.base.Splitter

/**
 * Created by s0041664 on 8/18/2017.
 */
class LibraryFactoryParser {
    def splitOn = "currentAttr ="       // default...create another constructor if override is needed
    def libraryText
    def libraryTextIterator

    LibraryFactoryParser(TextFile libraryFile) {
        parseFile(libraryFile)
    }

    def parseFile(TextFile libraryFile) {
        def fileText = libraryFile.getText()
        // split on string
        libraryText = (Splitter.on(splitOn)
                .split(fileText)).asList()
        // for all but first "chunk" put the splitter back at beginning
        libraryText = [libraryText[0]] + libraryText[1..libraryText.size()-1].collect { splitOn + it }
        libraryTextIterator = libraryText.iterator()
    }

    def hasNextTextBlock() {
        (libraryTextIterator.hasNext())
    }

    def nextTextBlock() {
        libraryTextIterator.next()
    }
}
