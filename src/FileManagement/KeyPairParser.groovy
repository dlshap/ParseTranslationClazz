package FileManagement
import com.google.common.base.Splitter

/**
 * Created by s0041664 on 8/11/2017.
 */

class KeyPairParser {
    static parseLineToMap(line) {

        Map<String, String> result = Splitter.on(',')
                .trimResults()
                .withKeyValueSeparator(
                Splitter.on('=')
                        .limit(2)
                        .trimResults())
                .split(line);
    }
}
