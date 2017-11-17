package logging

import java.text.SimpleDateFormat

/**
 * Created by s0041664 on 8/23/2017.
 */
class Dates {

    static currentDateAndTime() {
        new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date())
    }
}
