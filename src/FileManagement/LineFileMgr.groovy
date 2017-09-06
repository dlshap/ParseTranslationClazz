package FileManagement
/**
 * Created by s0041664 on 8/11/2017.
 */
class LineFileMgr extends FileMgr {
    def lines
    Iterator lineIterator

    def LineFileMgr() {
        super()
    }

    def LineFileMgr(fileName) {
        super(fileName)
    }

    def LineFileMgr(String fileName, createFlag create) {
        super(fileName, create)
    }

    def openFile(fileName) {
        super.openFile(fileName)
        if (theFile.length()) {
            lines = theFile.readLines()
            lineIterator = lines.iterator()
        }
        theFile
    }

    def next() {
        if (lineIterator.hasNext())
            lineIterator.next()
        else
            null
    }

    def hasNext() {
        lineIterator.hasNext()
    }

    def size() {
        lines.size()
    }

    /* output files */
    def writeLine(aLine) {
        theFile << aLine + "\r\n"
//        theFile << aLine + "\n"
    }
}
