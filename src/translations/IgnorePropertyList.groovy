package translations

import filemanagement.TextFile

class IgnorePropertyList {

    private ignorePropertyMsgFile
    private ignorePropertyMsgList
    def ignorePropertyList

    def IgnorePropertyList(filename) {
        ignorePropertyMsgFile = new TextFile(filename)
        buildIgnorePropertyList()
    }

    private def buildIgnorePropertyList() {
        loadTextFromIgnoreMsgFile()
        parseIgnorePropertyListFromMsgText()
    }

    private loadTextFromIgnoreMsgFile() {
        ignorePropertyMsgList = ignorePropertyMsgFile.getText()
    }

    private parseIgnorePropertyListFromMsgText() {
        // Example: get property name: xxx from msg like: "Property 'xxx' not found..."
        // Ignore lines beginning with "#"
        if (ignorePropertyMsgList != null) {
            def ignoreMsgList = ignorePropertyMsgList =~ /[^#]Property.+?'(.*?)'.*/
            ignorePropertyList = ignoreMsgList.collect { it[1] }
        }
    }

    def contains(val) {
        if (this.ignorePropertyList == null)
            false
        else
            (val in this.ignorePropertyList)
    }
}