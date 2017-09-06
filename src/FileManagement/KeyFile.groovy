package FileManagement

/**
 * Created by s0041664 on 8/11/2017.
 */
class KeyFile extends LineFileMgr {
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

    def next() {
        if (keyMapIterator.hasNext())
            keyMapIterator.next()
        else
            null
    }

    def rewindToStart() {
        keyMapIterator = keyMaps.iterator()
    }

    def hasNext() {
        if (keyMapIterator == null)
            null
        else
            keyMapIterator.hasNext()
    }

    def buildKeyMapList() {
        keyMaps = []
        for (int i = 0; i < super.lines.size; i++) {
            def parsedLine = KeyPairParser.parseToMap(super.lines[i])
            keyMaps << parsedLine
        }
        rewindToStart()
    }

    def getKeyMapSize() {
        keyMaps.size()
    }

    def getKeyMaps() {
        keyMaps
    }
}
