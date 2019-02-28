package filemanagement

import exceptions.OverwriteFileException
import i18n.Messages

/**
 * Created by s0041664 on 8/18/2017.
 */
class BaseFile {

    // this probably needs cleaning

    final enum CreateFlag {
        CREATE, CREATE_ONLY_IF_NO_EXISTING_FILE
    }

    String fileName
    File file

    BaseFile() {
    }

    static createFile(String fileName, CreateFlag createFlag) {
        BaseFile baseFile = new BaseFile()
        baseFile.fileName = fileName
        baseFile.createOrOverwriteFile(createFlag)
        baseFile
    }

    def openFile(String fileName, CreateFlag createFlag) {
        this.fileName = fileName
        createOrOverwriteFile(createFlag)
    }

    private createOrOverwriteFile(CreateFlag createFlag) {
        makeDirectories()
        if (file.exists() && createFlag != CreateFlag.CREATE_ONLY_IF_NO_EXISTING_FILE)
            file.delete()
    }

    private makeDirectories() {
        file = new File(fileName)
        File parentDir = new File(file.getParent())
        if (!parentDir.exists()) {
            parentDir.mkdirs()
            file = new File(fileName)
        }
    }

    BaseFile(fileName) {
        openFile(fileName)
    }

    BaseFile(fileName, CreateFlag createFlag) {
        this.fileName = fileName
        this.createOrOverwriteFile(createFlag)
    }

    def setFileUsingChooser(prompt, filePath) {
        file = FileChooser.chooseFile(prompt, filePath)
    }

    def openFile(fileName) {
        file = new File(fileName)
    }

    def getDirPath() {
        file.getParent()
    }

    def getFileName() {
        file == null ? null : file.getName()
    }

    def delete() {
        if (file.length())
            file.delete()
    }
}
