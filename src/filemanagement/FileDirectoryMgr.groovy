package filemanagement

/**
 * Created by s0041664 on 8/15/2017.
 */
class FileDirectoryMgr {

    def filePath = ""

    def FileDirectoryMgr(filePath) {
        this.filePath = filePath
    }

    def getFileList() {
        def fileList = []
        new File(filePath).eachFile {
            fileList << it.getName()
        }
        fileList
    }

    static makeDirectory(filePath) {
        def newPath = new File(filePath)
        if (!newPath.exists())
            newPath.mkdir()
    }


    static getSmallName(fileName) {
        // remove the file extension
        def fullName = new File(fileName).getName()
        def smallName = fullName - ~/\..*/
        smallName
    }
}
