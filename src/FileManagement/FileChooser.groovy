package FileManagement

import javax.swing.JFileChooser

/**
 * Created by s0041664 on 8/24/2017.
 */
class FileChooser {

    enum selectMode {BOTH, DIRECTORIES}

    static chooseFile(title, fp, selectMode mode) {
        String fullPathName
        File chosenFile
        JFileChooser fc = new JFileChooser(fp)
        def selectMode = (mode == selectMode.BOTH) ? JFileChooser.FILES_AND_DIRECTORIES : JFileChooser.DIRECTORIES_ONLY
        fc.setDialogTitle(title)
        fc.setFileSelectionMode(selectMode)
        int result = fc.showOpenDialog(null)
        switch (result) {
            case JFileChooser.APPROVE_OPTION:
                chosenFile = fc.getSelectedFile()
                break
            case JFileChooser.CANCEL_OPTION:
            case JFileChooser.ERROR_OPTION:
                break
        }
        if (chosenFile != null)
            fullPathName = chosenFile.getPath()
        fullPathName
    }

    static chooseFile(title, fp) {
        chooseFile(title, fp, selectMode.BOTH)
    }
}
