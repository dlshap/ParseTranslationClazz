package FileManagement
/**
 * Created by s0041664 on 8/11/2017.
 */
class LineFile {
    def lines
    File lineFile
    Iterator lineIterator

    def LineFile(fileName) {
        openFile(fileName)
    }

    def LineFile(fileName, newFileFlag) {
        openFile(fileName)
        if (lineFile.exists()) {
            lineFile.delete()
        }
    }

    def openFile(fileName) {
        lineFile = new File(fileName)
        if (lineFile.length()) {
            lines = lineFile.readLines()
            lineIterator = lines.iterator()
        }
        lineFile
    }

    def exists() {
        (lines != null)
    }

    def getDirPath() {
        lineFile.getParent()
    }

    def getPath() {
        lineFile.getPath()
    }

    def getFileName() {
        lineFile.getName()
    }

    def nextLine() {
        if (lineIterator.hasNext())
            lineIterator.next()
        else
            null
    }

    def hasNext() {
        lineIterator.hasNext()
    }

    /* output files */
    def writeLine(aLine) {
        lineFile << aLine + "\r\n"
    }

    def delete() {
        if (lineFile.length())
            lineFile.delete()
    }


}
