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

    BaseFile(fileName) {
        openFile(fileName)
    }

    BaseFile(fileName, CreateFlag createFlag) {
        openFile(fileName, createFlag)
    }

    def openFile(fileName) {
        file = new File(fileName)
        if (file != null)
            this.fileName = fileName
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

    File chooseFile(prompt, filePath) {
        file = FileChooser.chooseFile(prompt, filePath)
    }

    String getDirPath() {
        file.getParent()
    }

    String getFileName() {
        file == null ? null : file.getName()
    }

    String getFullName() {
        file == null ? null : this.fileName
    }

    def delete() {
        if (file.length())
            file.delete()
    }
}
