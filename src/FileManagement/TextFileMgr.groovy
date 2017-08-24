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

    def setText(newText) {
        theFile.delete()
        theFile.text = newText
        fileText = newText
    }

    def delete() {
        fileText.delete()
    }

    def writeToFile(text) {
        theFile << text
    }

    def makeBackupFile() {
        if (theFile.exists()) {
            File backupFile = new File(theFile.getPath()+".backup")
            if (backupFile.exists())
                backupFile.delete()
            backupFile.text = this.getText()
        }
     }

}
