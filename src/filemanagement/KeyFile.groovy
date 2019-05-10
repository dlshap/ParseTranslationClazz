package filemanagement

/**
 * Created by s0041664 on 8/11/2017.
 */
class KeyFile extends LineFile {
    Map keyMap = [:]

    KeyFile(fileName) {
        super()
        openFile(fileName)
    }

    def openFile(fileName) {
        super.openFile(fileName)
        buildKeyMap()
    }

    def buildKeyMap() {
        if (!(super.lines == null)) {
            for (int i = 0; i < super.lines.size; i++) {
                if ((super.lines[i]).contains("=")) {
                    def parsedLine = KeyPairParser.parseToMap(super.lines[i])
                    keyMap << parsedLine
                }
            }
        }
    }

    def get(String key) {
        keyMap[key]
    }
}

