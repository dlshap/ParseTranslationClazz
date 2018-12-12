package libraryquestions

import filemanagement.TextFile
import com.google.common.base.Splitter

/**
 * Created by s0041664 on 8/18/2017.
 */
class LibraryFactoryParser {
    def splitOn = "currentAttr ="       // default...create another constructor if override is needed
    def libraryText
    def libraryIterator

    def LibraryFactoryParser(TextFile libraryFile) {
        parseFile(libraryFile)
    }

    def parseFile(TextFile libraryFile) {
        def fileText = libraryFile.getText()
        // split on string
        libraryText = (Splitter.on(splitOn)
                .split(fileText)).asList()
        // for all but first "chunk" put the splitter back at beginning
        def allButFirst = libraryText[1..libraryText.size()-1].collect { splitOn + it }
        libraryText = [libraryText[0]]
        allButFirst.each {libraryText << it}
        libraryIterator = libraryText.iterator()
    }

    def hasNext() {
        (libraryIterator.hasNext())
    }

    def next() {
        libraryIterator.next()
    }
}
