package logging

class LogUtils {

    static def startLogging(String name, String path) {
        Log.open path + "logs\\$name-log.txt"
        Log.writeLine"Starting $name utility at ${Dates.currentDateAndTime()}:\r\n"
    }

    static def openLogs(String logPath, String logName) {
        def logsFilePath = logPath
        Log.open("adds", logsFilePath + "$logName-adds-log.txt")
        Log.writeLine "adds", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("updates", logsFilePath + "$logName-changes-log.txt")
        Log.writeLine "updates", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("deletes", logsFilePath + "$logName-deletes-log.txt")
        Log.writeLine "deletes", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }
}
