package filemanagement

class IgnoreFile extends LineFile {

    ArrayList<String> ignoreList

    IgnoreFile(String fileName) {
        this.openFile(fileName)
        this.buildIgnoreList()
    }

    ArrayList<String> buildIgnoreList() {
        ignoreList = []
        while (this.hasNext()) {
            String ignoreLine = this.next()
            if (ignoreLine.contains("=")) {
                ArrayList<String> ignoreKey = ignoreLine.split("=")
                ignoreList << ignoreKey[0]
            } else {
                ignoreList << ignoreLine
            }
        }
        ignoreList
    }


}
