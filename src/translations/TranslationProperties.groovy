package translations
/**
 * Created by s0041664 on 8/25/2017.
 */
class TranslationProperties {

    Map propertyMap = [:]
    Iterator<Map> propertyIterator

    TranslationProperties(ArrayList<String> propertyLines) {
        this.buildPropertyMapForAllProperties(propertyLines)
    }

    def buildPropertyMapForAllProperties(ArrayList<String> propertyLines) {
        def lastOtherPropertyIndex = 1
        def propKey

        def propertyLineIter = propertyLines.iterator()
        while (propertyLineIter.hasNext()) {
            String nextPropLine = propertyLineIter.next()

            if ((nextPropLine.indexOf("=") == -1) || (nextPropLine[0] == "#")) {
                propKey = "*OTHER" + lastOtherPropertyIndex++
                propertyMap[propKey] = nextPropLine
            } else {
                def parsedProp = nextPropLine.split("=")
                if (parsedProp.size() >= 2) {
                    propertyMap[parsedProp[0].trim()] = parsedProp[1]
                } else
                    propertyMap[parsedProp[0].trim()] = ""
            }
        }
        propertyIterator = propertyMap.iterator()
    }

    String get(keyName) {
        propertyMap[keyName]
    }

//    TODO: remove
//    def set(keyName, newValue) {
//        propertyMap[keyName] = newValue
//    }

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
