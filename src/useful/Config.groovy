package useful

import filemanagement.KeyFile

class Config {

    KeyFile configFile

    Config(path) {
        configFile = new KeyFile(path+"\\config.properties")
    }

    String get(String key) {
        configFile.get(key)
    }


}
