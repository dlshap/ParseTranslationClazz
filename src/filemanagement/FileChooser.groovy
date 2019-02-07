package filemanagement

import javax.swing.JFileChooser

/**
 * Created by s0041664 on 8/24/2017.
 */
class FileChooser {
    static chooseFile(title, pathName) {
        openChooser(title, pathName, JFileChooser.FILES_AND_DIRECTORIES)
    }

    static chooseDirectory(title, pathName) {
        openChooser(title, pathName, JFileChooser.DIRECTORIES_ONLY)
    }

    private static openChooser(title, pathName, mode) {
        def fileChooser = new JFileChooser(pathName)
        fileChooser.setDialogTitle(title)
        fileChooser.setFileSelectionMode(mode)
        fileChooser.showOpenDialog(null)
        fileChooser.getSelectedFile()
    }

    static chooseFileAndReturnFilename(title, pathName) {
        def file = chooseFile(title, pathName)
        getFileNameFromFile(file)
    }

    static chooseDirectoryAndReturnDirName(title, pathName) {
        def file = chooseDirectory(title, pathName)
        getFileNameFromFile(file)
    }

    static getFileNameFromFile(File file) {
        file == null ? null : file.getPath()
    }

}
