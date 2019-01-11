package translations

class Translation {

    def transKeyMap = [:]

    def Translation(transKeyMap) {
        this.transKeyMap = transKeyMap
    }

    def get(keyName) {
        transKeyMap[keyName]
    }
}