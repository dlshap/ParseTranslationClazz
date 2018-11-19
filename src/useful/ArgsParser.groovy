package useful

class ArgsParser {
    static buildArgsMap(args) {
        def key, value
        def argsMap = [:]
        def argCount = args.size()
        args.each {
            def match = it =~ /(.*?)=(.*)/
            if (match[0].size() > 0) {
                key = match[0][1]
                value = match[0][2]
                argsMap.put(key, value)
            }
        }
        argsMap
    }
}
