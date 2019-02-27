package excelfilemanagement

import org.apache.poi.ss.usermodel.Workbook

class ExcelWorkbook {

    Workbook workbook
    Iterator sheetIterator

    def workbookStream

    def write() {
        workbook.write(workbookStream)
    }

    def close() {
        workbookStream.close()
    }

    def resetSheets() {
        sheetIterator = workbook.iterator()
    }

    def hasNextSheet() {
        sheetIterator.hasNext()
    }

    def nextSheet() {
        sheetIterator.next()
    }
}
