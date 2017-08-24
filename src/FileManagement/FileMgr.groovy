package FileManagement

/**
 * Created by s0041664 on 8/18/2017.
 */
class FileMgr {

    enum createFlag {CREATE}
    File theFile

    def FileMgr(fileName) {
        openFile(fileName)
    }

    def FileMgr(fileName, createFlag create) {
        openFile(fileName)
        if (theFile.exists()) {
            theFile.delete()
        }
    }

    def openFile(fileName) {
        theFile = new File(fileName)
    }

    def exists() {
        (theFile.exists())
    }

    def getDirPath() {
        theFile.getParent()
    }

    def getFullPathName() {
        theFile.getPath()
    }

    def getFileName() {
        theFile.getName()
    }

    def delete() {
        if (theFile.length())
            theFile.delete()
    }
}
