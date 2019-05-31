package properties

import excelfilemanagement.SpreadsheetArgs

class PropertyArgs extends SpreadsheetArgs{

    String propertyFilePath
    String propertySpreadsheetLogPath
    String propertyFileLogPath

    PropertyArgs(args) {
        super(args)
    }

    def getConfigValues() {
        super.getConfigValues()
        spreadsheetPath = basePath + config.get("property.spreadsheet.relative.path")
        propertySpreadsheetLogPath = basePath + config.get("property.spreadsheet.log.relative.path")
        propertyFileLogPath = basePath + config.get("property.file.log.relative.path")
        propertyFilePath = basePath
    }
}
