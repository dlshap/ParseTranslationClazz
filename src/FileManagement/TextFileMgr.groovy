package FileManagement

/**
 * Created by s0041664 on 8/18/2017.
 */
class TextFileMgr extends FileMgr {

    def fileText

    def TextFileMgr(fileName) {
        super(fileName)
    }

    def TextFileMgr(fileName, FileMgr.createFlag create) {
        super(fileName, create)
    }

    def openFile(fileName) {
        super.openFile(fileName)
        if (theFile.length()) {
            fileText = theFile.text
        }
        theFile
    }

    def getText() {
        fileText
    }

    def writeToFile(text) {
        theFile << text
    }

}
