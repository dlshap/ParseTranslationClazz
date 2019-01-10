package translations

class TranslationFieldKeys {
    def keys = [:]

    def TranslationFieldKeys(keyMap) {
        this.keys = keyMap
    }

    def getKeyValue(keyName) {
        keys.get(keyName)
    }

    def getKeys() {
        keys
    }

    def getKeyList() {
        keys.collect().toString()[1..-2]    //printable list of keys and values
    }
}
