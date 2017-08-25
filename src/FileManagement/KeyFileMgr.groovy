package FileManagement

/**
 * Created by s0041664 on 8/11/2017.
 */
class KeyFileMgr extends LineFileMgr {
    def keyMaps = []

    def KeyFileMgr(fileName) {
        super(fileName)
        this.buildKeyMapList()
    }

    def nextLine() {
        if (super.hasNext())
            KeyPairParser.parseLine(super.nextLine())
        else
            null
    }

    def buildKeyMapList() {
        keyMaps = []
        for (int i = 0; i < super.lines.size; i++) {
            keyMaps << KeyPairParser.parseToMap(super.lines[i]);
        }
    }

    def getKeyMaps() {
        keyMaps
    }
}
