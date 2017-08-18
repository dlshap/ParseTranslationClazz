package LibraryQuestions

import FileManagement.TextFileMgr
import com.google.common.base.Splitter
import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator

/**
 * Created by s0041664 on 8/18/2017.
 */
class LibraryFileParser {
    def splitOn = "currentAttr ="       // default...create another constructor if override is needed
    def libraryText
    def libraryIterator

    def LibraryFileParser(TextFileMgr libraryFile) {
        parseFile(libraryFile)
    }

    def parseFile(TextFileMgr libraryFile) {
        def fileText = libraryFile.getText()
        // split on string
        def libraryText = (Splitter.on(splitOn)
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
