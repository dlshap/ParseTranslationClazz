//def keyMap = ["A":"AK", "B":"BK"]
//def bigList = [["A":"AK", "B":"BK", "C":"C1"]] //match both
//bigList << ["A":"AK", "B":"BN", "C":"C2"] //match A
//bigList << ["A":"AN", "B":"BK", "C":"C3"] //match B
//bigList << ["A":"AN", "B":"BN", "C":"C2"] //match neither
//bigList << ["A":"AK", "B":"BK", "C":"C5"] //match both
//
//def newList = bigList
//
//keyMap.each {
//    k,v -> newList = newList.findAll {it.get(k) == keyMap.get(k)}
//}
//
//def keyList = keyMap.collect()
//println "Key List: "+keyList.toString()[1..-2]
//println newList
//println newList.size()

def filePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations"
println filePath[-15..-1]
if (filePath[-1] != "\\")
    filePath += "\\"

println filePath

/************************************************************************************************************************************/



