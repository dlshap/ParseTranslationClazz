import FileManagement.FileChooser

class StripTranslatedFileNames {

    static stripTranslatedFromFileNames(changeLibrary) {
        println changeLibrary
    }

    static main(args) {
        def fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Translations\\\\"
        // pick library folder (translated files)
        def changeLibrary = FileChooser.chooseFile("Select Directory Folder for Name Changes", fp, FileChooser.selectMode.DIRECTORIES)
        if (changeLibrary != null) {
            stripTranslatedFromFileNames(changeLibrary)
        }
    }
}
