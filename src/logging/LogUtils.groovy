package logging

class LogUtils {

    static def OpenLogs(String logPath, String logName) {
        def logsFilePath = logPath
        Log.open("adds", logsFilePath + "$logName-log-property-adds.txt")
        Log.writeLine "adds", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("updates", logsFilePath + "$logName-log-property-changes.txt")
        Log.writeLine "updates", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("deletes", logsFilePath + "$logName-log-property-deletes.txt")
        Log.writeLine "deletes", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }
}
