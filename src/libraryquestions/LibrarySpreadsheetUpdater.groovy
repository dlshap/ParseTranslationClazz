package libraryquestions

import properties.ExcelPropertyFile
import properties.ExcelPropertySheet

class LibrarySpreadsheetUpdater extends LibrarySpreadsheetBuilder {

    ExcelPropertyFile oldLibraryExcelFile

    LibrarySpreadsheetUpdater(ExcelPropertyFile origLibraryExcelFile) {
        this.oldLibraryExcelFile = origLibraryExcelFile
        String pathName = origLibraryExcelFile.file.getParent() + "\\"
        String newLibraryFileName = pathName + (origLibraryExcelFile.fileName).replace(")", ")_new")
        this.createNewLibraryExcelFileFromFileName(newLibraryFileName)
    }

    def updateSpreadsheetFromModel(ExcelPropertyFile modelLibraryExcelFile) {
//        while (modelLibraryExcelFile.hasNextExcelPropertySheet()) {
//            ExcelPropertySheet modelSheet = modelLibraryExcelFile.nextExcelPropertySheet()
//            String sheetName = modelSheet.sheetName
//            ExcelPropertySheet origSheet = oldLibraryExcelFile.getExcelPropertySheet(sheetName)
//            if (origSheet == null) {
//                buildLanguageSheetFromModelSheet(modelSheet)
//            } else {
//                updateNewSheetFromModelAndOrig(modelSheet, origSheet)
//            }
//            print "." // for impatient users
//        }
        newLibraryExcelFile.writeAndClose()
        renameOldAndNewLibraryFiles()
    }

    private renameOldAndNewLibraryFiles() {
        String pathName = oldLibraryExcelFile.file.getParent() + "\\"
        renameOrigFileToOld(pathName)
        renameNewFileToOrig(pathName)
    }

    private renameOrigFileToOld(String pathName) {
        String origFileName = oldLibraryExcelFile.fileName
        String oldFileName = origFileName.replace(")", ")_old")
        File oldFile = new File(pathName + oldFileName)
        if (oldFile.exists())
            oldFile.delete()
        File origFile = new File(pathName + oldFileName)
        origFile.renameTo(pathName + oldFileName)
    }

    private renameNewFileToOrig(String pathName) {
        String newFileName = newLibraryExcelFile.fileName
        String origFileName = oldLibraryExcelFile.fileName
        File newFile = new File(pathName + newFileName)
        File origFile = new File(pathName + origFileName)
        newFile.renameTo(origFile)
    }

    def updateNewSheetFromModelAndOrig(ExcelPropertySheet modelSheet, ExcelPropertySheet origSheet) {
        buildLanguageSheetFromModelSheet(modelSheet)
    }

}
