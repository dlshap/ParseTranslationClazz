package FileManagement

import javax.swing.JFileChooser

/**
 * Created by s0041664 on 8/24/2017.
 */
class FileChooser {

    def chooseFile(fp) {
//      return full pathname to selected file
        File chosenFile
        JFileChooser fc = new JFileChooser(fp)
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES)
        int result = fc.showOpenDialog(null)
        switch (result) {
            case JFileChooser.APPROVE_OPTION:
                chosenFile = fc.getSelectedFile()
                break
            case JFileChooser.CANCEL_OPTION:
            case JFileChooser.ERROR_OPTION:
                break
        }
        def fullPathName = chosenFile.getPath()
        fullPathName
    }

}
