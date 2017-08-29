package Translations

import FileManagement.KeyFileMgr
import FileManagement.KeyPairParser
import FileManagement.LineFileMgr

import javax.sound.sampled.Line

/**
 * Created by s0041664 on 8/25/2017.
 */
class Properties {

    LineFileMgr propFile
    Map propKeyMap = [:]

    def Properties(propFile) {
        this.propFile = propFile
        propKeyMap = buildPropKeyMap(propFile)
    }

    static buildPropKeyMap(LineFileMgr propFile) {
        Map<String, String> keyMap = [:]
        def lastNonProperty = 1
        def propKey

        while (propFile.hasNext()) {
            String nextPropLine = propFile.next()

            if (nextPropLine.indexOf("=") == -1) {
                propKey = "*OTHER" + lastNonProperty++
                keyMap[propKey] = nextPropLine
            } else {
                def parsedProp = nextPropLine.split("=")
                keyMap[parsedProp[0]] = parsedProp[1]
            }
        }
        keyMap
    }

    def writeToFile(LineFileMgr propkeyFile) {
        if (propkeyFile != null) {
            propKeyMap.each {propKey, propValue ->
                if (propKey[0] == "*") {
                    propkeyFile.writeLine(propValue)
                } else {
                    propkeyFile.writeLine(propKey + "=" + propValue)
                }
            }
        }
    }

    def getPropertyValue(keyValue) {
        propKeyMap[keyValue]
    }

    def setPropertyValue(keyValue, newValue) {
        propKeyMap[keyValue] = newValue
    }
}
