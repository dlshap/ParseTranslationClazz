package translations

import filemanagement.LineFile
import filemanagement.PropertyFile

/**
 * Created by s0041664 on 8/25/2017.
 */
class Properties {

    PropertyFile propertyFile
    Map allProperties = [:]
    Iterator<Map> propertyIterator

    def Properties() {
        // no property file
    }

    def Properties(propertyFile) {
        this.propertyFile = propertyFile
        allProperties = buildAllPropertiesMap(this.propertyFile)
        propertyIterator = allProperties.iterator()
    }

    static buildAllPropertiesMap(PropertyFile propFile) {
        Map property = [:]
        def lastOtherPropertyIndex = 1
        def propKey

        while (propFile.hasNext()) {
            String nextPropLine = propFile.next()

            if ((nextPropLine.indexOf("=") == -1) || (nextPropLine[0] == "#")) {
                propKey = "*OTHER" + lastOtherPropertyIndex++
                property[propKey] = nextPropLine
            } else {
                def parsedProp = nextPropLine.split("=")
                if (parsedProp.size() >= 2) {
                    property[parsedProp[0].trim()] = parsedProp[1]
                } else
                    property[parsedProp[0].trim()] = ""
            }
        }
        property
    }

    def getProperties() {
        allProperties
    }

    def setProperties(Map properties) {
        this.allProperties = properties
    }

    def writePropertiesToTranslatedOutputFile() {
        LineFile translatedFile = propertyFile.openTranslatedOutputFile()
        if (propertyFile != null) {
            allProperties.each { propKey, propValue ->
                if (propKey[0] == "*") {
                    translatedFile.writeLine(propValue)
                } else {
                    translatedFile.writeLine(propKey + "=" + propValue)
                }
            }
        }
    }

    def get(keyName) {
        allProperties[keyName]
    }

    def set(keyName, newValue) {
        allProperties[keyName] = newValue
    }

    def rewind() {
        propertyIterator = allProperties.iterator()
    }

    def hasNext() {
        if (propertyIterator != null)
            propertyIterator.hasNext()
        else
            null
    }

    def next() {
        if (propertyIterator != null)
            propertyIterator.next()
        else
            null
    }
}
