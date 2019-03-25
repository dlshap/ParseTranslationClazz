package libraryquestions

import logging.Dates
import logging.Log

class LibraryLogs {
    static openLogs(libraryArgs) {
        def logsFilePath = libraryArgs.libraryFilePath + "logs\\\\"
        Log.open logsFilePath + "log-library-translations.txt"
        Log.writeLine "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open "exceptions", logsFilePath + "log-library-exceptions.txt"
        Log.writeLine "exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open "nocode", logsFilePath + "log-library-nocode.txt"
        Log.writeLine "nocode", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    static closeLogs() {
        Log.writeLine("\r\nDone at: " + Dates.currentDateAndTime())
        Log.writeLine("exceptions", "\r\nDone at: " + Dates.currentDateAndTime())
        Log.writeLine("nocode", "\r\nDone at: " + Dates.currentDateAndTime())
    }

}
