package logging

import filemanagement.LineFile
import filemanagement.BaseFile

/**
 * Created by s0041664 on 8/16/2017.
 */
class Log {
    static file = [:]

    static open(logName, fileName) {
        file[logName] = new LineFile(fileName, BaseFile.CreateFlag.CREATE)
    }

    static open(fileName) {
        open("default", fileName)
    }

    static writeLine(logName, aLine) {
        println aLine
        if (file[logName] != null)
            file[logName].writeLine aLine
    }

     static writeLine(aLine) {
        writeLine("default", aLine)
    }
}
