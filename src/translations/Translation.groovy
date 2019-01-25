package translations

class Translation {
    //force recompile

    def transKeyMap = [:]

    def Translation(transKeyMap) {
        this.transKeyMap = transKeyMap
    }

    def get(keyName) {
        transKeyMap[keyName]
    }
}
