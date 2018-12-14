package translations

class TranslationFieldKeys {
    def keys = [:]

    def TranslationFieldKeys(keyMap) {
        this.keys = keyMap
    }

    def getKey(keyName) {
        keys.get(keyName)
    }

}
