package Logging

import FileManagement.LineFile

/**
 * Created by s0041664 on 8/16/2017.
 */
class Log {
    static file

    static open(fileName) {
        file = new LineFile(fileName, "create")
    }

    static writeLine(aLine) {
        println aLine
        if (file != null)
            file.writeLine aLine
    }
}
