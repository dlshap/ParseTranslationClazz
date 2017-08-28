package FileManagement

/**
 * Created by s0041664 on 8/11/2017.
 */
class KeyFileMgr extends LineFileMgr {
    def keyMaps = []
    def keyMapIterator

    def KeyFileMgr(fileName) {
        super(fileName)
        buildKeyMapList()
    }

    def next() {
        if (keyMapIterator.hasNext())
            keyMapIterator.next()
        else
            null
    }

    def hasNext() {
        keyMapIterator.hasNext()
    }

    def buildKeyMapList() {
        keyMaps = []
        for (int i = 0; i < super.lines.size; i++) {
            def parsedLine = KeyPairParser.parseToMap(super.lines[i])
            keyMaps << parsedLine
        }
        keyMapIterator = keyMaps.iterator()
    }

    def getKeyMapSize() {
        keyMaps.size()
    }

    def getKeyMaps() {
        keyMaps
    }
}
