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
}
