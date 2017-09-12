package FileManagement

/**
 * Created by s0041664 on 8/11/2017.
 */
class KeyFile extends LineFile {
    def keyMaps = []
    def keyMapIterator

    def KeyFile() {
        super()
    }

    def KeyFile(fileName) {
        super()
        openFile(fileName)
    }

    def openFile(fileName) {
        super.openFile(fileName)
        buildKeyMapList()
    }

    def buildKeyMapList() {
        keyMaps = []
        for (int i = 0; i < super.lines.size; i++) {
            def parsedLine = KeyPairParser.parseToMap(super.lines[i])
            keyMaps << parsedLine
        }
        rewind()
    }

    def next() {
        if (keyMapIterator.hasNext())
            keyMapIterator.next()
        else
            null
    }

    def rewind() {
        keyMapIterator = keyMaps.iterator()
    }

    def hasNext() {
        if (keyMapIterator == null)
            null
        else
            keyMapIterator.hasNext()
    }

    def getKeyMapSize() {
        keyMaps.size()
    }

    def getKeyMaps() {
        keyMaps
    }
}
