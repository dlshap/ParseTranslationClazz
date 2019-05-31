package utilities

class RenameFiles {

    def REALRUN = false

    def REPLACE = false

    def pathName = "C:\\Users\\s0041664\\Documents\\Projects\\Project Status\\Rules Status"

    String path, oldname, newname

    RenameFiles() {
        start()
    }

    def renameWithReplace() {
        newname = oldname.replaceAll(/EFSP/, "ADS")
    }

    def renameWithRegex() {
        def pat = /(.*? Sprint 19-)(\d\d)(.*?v\d.)(\d).*- Copy(\..*)/
        def matcher = oldname =~ pat
        if (matcher.size() == 1) {
            def fn = matcher[0]
            if (fn.size() >= 2) {
                String sprintNumber = (sprintf("%02d", (fn[2]).toInteger() + 1))
                newname = "${fn[1]}$sprintNumber${fn[3]}1${fn[5]}"
            }
        }
    }

    static main(args) {
        new RenameFiles()
    }

    def start() {
        def dir = new File(pathName)
        dir.eachFile {
            path = it.getParent()
            oldname = it.getName()
            newname = ""
            if (REPLACE)
                renameWithReplace()
            else
                renameWithRegex()
            if (newname != "") {
                println "$it .renameTo(${path + "\\" + newname})"
                if (REALRUN)
                    it.renameTo("${path + "\\" + newname}")
            }
        }
    }
}
