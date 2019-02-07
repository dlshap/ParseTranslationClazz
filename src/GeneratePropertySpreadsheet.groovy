import libraryquestions.LibraryArgs

class GeneratePropertySpreadsheet {

    GeneratePropertySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GeneratePropertySpreadsheet(args)
    }

    def start(args) {
        def libraryArgs = new LibraryArgs(args)
        generateSpreadsheet(libraryArgs)
    }

    def generateSpreadsheet(libraryArgs) {

    }


}
