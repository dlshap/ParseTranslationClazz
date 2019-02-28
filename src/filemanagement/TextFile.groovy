package filemanagement

/**
 * Created by s0041664 on 8/18/2017.
 */
class TextFile extends BaseFile {

    def fileText

    def TextFile(fileName) {
        super(fileName)
    }

    def TextFile(fileName, CreateFlag create) {
        super(fileName, create)
    }

    def openFile(fileName) {
        super.openFile(fileName)
        if (file.length()) {
            fileText = file.text
        }
        file
    }

    def getText() {
        fileText
    }

    def setText(newText) {
        file.delete()
        file.text = newText
        fileText = newText
    }

    def delete() {
        fileText.delete()
    }

    def writeToFile(text) {
        file << text
    }

    def makeBackupFile() {
        if (file.exists()) {
            File backupFile = new File(file.getPath()+".backup")
            if (backupFile.exists())
                backupFile.delete()
            backupFile.text = this.getText()
        }
     }
}
