package filemanagement

import exceptions.OverwriteFileException
import useful.Messages

/**
 * Created by s0041664 on 8/18/2017.
 */
class BaseFile {

    // this probably needs cleaning

    final enum createFlag {
        CREATE, CREATE_ONLY_IF_NO_EXISTING_FILE
    }

    File file

    BaseFile() {
    }

    BaseFile(fileName) {
        openFile(fileName)
    }

    BaseFile(fileName, createFlag create) {
        openFile(fileName, create)
    }

    def openFile(fileName) {
        file = new File(fileName)
    }

    def openFile(fileName, createFlag create) {
        openFile(fileName)
        if (file.exists()) {
            if (create == createFlag.CREATE)
                file.delete()
            else
                throw new OverwriteFileException(Messages.getString("exception.message.overwrite.file"))
        }
    }

    def exists() {
        ((file.exists()) && (file != null))
    }

    def getDirPath() {
        file.getParent()
    }

    def getFileName() {
        file.getName()
    }

    def delete() {
        if (file.length())
            file.delete()
    }
}

//    def getFullPathName() {
//        file.getPath()
//    }
//
