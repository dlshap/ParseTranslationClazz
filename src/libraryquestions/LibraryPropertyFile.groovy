package libraryquestions

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import properties.ExcelPropertyFile

import java.lang.reflect.Array

class LibraryPropertyFile extends ExcelPropertyFile {

    ArrayList<String> classNames = []

    LibraryPropertyFile() {
    }

    static openLibraryPropertyFileUsingChooser(String prompt, LibraryArgs libraryArgs) {
        LibraryPropertyFile libraryPropertyFile = new LibraryPropertyFile()
        libraryPropertyFile.buildLibraryPropertyFile(prompt, libraryArgs)
        libraryPropertyFile
    }

    def buildLibraryPropertyFile(String prompt, LibraryArgs libraryArgs) {
        chooseLibraryPropertyFile(prompt, libraryArgs.startFilePath)
        loadClassNamesFromSheets(libraryArgs.fileNameForTestingSingleFile)
    }

    def chooseLibraryPropertyFile(prompt, filePath) {
        def startDir = new File(filePath)
        def spreadsheetDirectory = startDir.getParent() + "\\Spreadsheets\\DMTQuestionLibrarySpreadsheets\\"
        chooseFile(prompt, spreadsheetDirectory)
    }

    def loadClassNamesFromSheets(String fileNameForTestingSingleFile) {
        classNames = workbook.sheetIterator().collect() {it.sheetName}
        classNames.removeAll() {it.contains("Table of Contents")}
        if (fileNameForTestingSingleFile != null)
            classNames.removeAll() {(!(it.equals(fileNameForTestingSingleFile)))}
        println classNames.size()
    }

    def getClassNameList() {
        (classNames.collect().toString())[1..-2]
    }

    def getClassNameCount() {
        classNames.size()
    }

    def getSheet(String sheetName) {
        workbook.getSheet(sheetName)
    }
}
