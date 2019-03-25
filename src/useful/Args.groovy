package useful

class Args {
    def argsMap = [:]

    Args(args) {
        def argsKey, argsValue
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

    def set(key, value) {
        if (argsMap.get(key) != null)
            argsMap.replace(key, value)
        else
            argsMap.put(key, value)
    }
}
