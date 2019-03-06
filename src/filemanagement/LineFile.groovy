package filemanagement
/**
 * Created by s0041664 on 8/11/2017.
 */
class LineFile extends BaseFile {
    ArrayList<String> lines
    Iterator lineIterator

    LineFile() {
    }

    LineFile(String fileName, CreateFlag create) {
        super(fileName, create)
    }

    def openFile(fileName) {
        super.openFile(fileName)
        if (file.length() > 0) {
            lines = file.readLines()
            lineIterator = lines.iterator()
        }
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
        file << aLine + "\r\n"
//        file << aLine + "\n"
    }
}
