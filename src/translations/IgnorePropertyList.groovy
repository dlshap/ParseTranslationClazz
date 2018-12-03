package translations

import filemanagement.TextFile

class IgnorePropertyList {

    private ignorePropertyListFile
    def ignorePropertyList

    def IgnorePropertyList(filename) {
        ignorePropertyListFile = new TextFile(filename)
        loadIgnorePropertyList()
    }

    private def loadIgnorePropertyList() {
        def ignoreList = ignorePropertyListFile.getText()
        if (ignoreList != null) {
            def matchingLines = ignoreList.findAll(/[^#]Property.+?'.*?'.*/).collect()
            ignorePropertyList = matchingLines.collect { (it =~ /Property.+?'(.*?)'.*/)[0][1] }
        }
    }

    def contains(val) {
        if (this.ignorePropertyList == null)
            false
        else
            (val in this.ignorePropertyList)
    }
}