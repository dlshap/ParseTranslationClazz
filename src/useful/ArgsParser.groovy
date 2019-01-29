package useful

class ArgsParser {
    def argsMap = [:]

    ArgsParser (args) {
        def argsKey, argsValue
        def argCount = args.size()
        args.each {
            def argsMatch = it =~ /(.*?)=(.*)/
            if (argsMatch[0].size() > 0) {
                argsKey = argsMatch[0][1]
                argsValue = argsMatch[0][2]
                argsMap.put(argsKey, argsValue)
            }
        }
    }

    def get(argsKey) {
        argsMap.get(argsKey)
    }
}
