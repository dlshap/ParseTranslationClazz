package logging

class LogUtils {

    static def startLogging(String name, String startPath) {
        String logName = fixName(name)
        Log.open startPath + "logs\\$logName-log.txt"
        Log.writeLine"Starting $logName utility at ${Dates.currentDateAndTime()}:\r\n"
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

    static String fixName(String name) {
        name.replaceFirst(/.*?\./, "")
    }
}
