package exceptions

class OverwriteFileException extends Exception {

    def information

    OverwriteFileException(String message) {
        information = message
    }
}
