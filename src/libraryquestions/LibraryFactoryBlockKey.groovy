package libraryquestions

class LibraryFactoryBlockKey {
    def keys = [:]

    def LibraryFactoryBlockKey(keyMap) {
        this.keys = keyMap
    }

    def getKey(keyName) {
        keys.get(keyName)
    }

}
