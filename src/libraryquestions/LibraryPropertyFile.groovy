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

    static openLibraryPropertyFileUsingChooser(prompt, filePath) {
        LibraryPropertyFile libraryPropertyFile = new LibraryPropertyFile()
        libraryPropertyFile.buildLibraryPropertyFile(prompt, filePath)
        libraryPropertyFile
    }

    def buildLibraryPropertyFile(prompt, filePath) {
        chooseLibraryPropertyFile(prompt, filePath)
        loadClassNamesFromSheets()
    }

    def chooseLibraryPropertyFile(prompt, filePath) {
        def startDir = new File(filePath)
        def spreadsheetDirectory = startDir.getParent() + "\\Spreadsheets\\DMTQuestionLibrarySpreadsheets\\"
        chooseFile(prompt, spreadsheetDirectory)
    }

    def loadClassNamesFromSheets() {
        classNames = workbook.sheetIterator().collect() {it.sheetName}
        classNames.removeAll() {it.contains("Table of Contents")}
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
