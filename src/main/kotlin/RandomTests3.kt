fun main() {
//    println(sigma(3))
}
fun sigma(int:Int): Int {
    return (1..int).fold(0,Int::plus)
}
private fun getSortedFrequencyMap(text: String/*listOfStringBuilders: MutableList<StringBuilder>*/): Map<String, Int?> {
    val listOfWords = text.split(" ", ".", ",", ";", ":", /*"\"",*/ "\'", "!", "?", "<", ">", "/", "\\", "%", "$", "#", "@", "^", "&", "*", "(", ")", "-", "+", "=", "{", "[", "]", "}", "`", "~", "_", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0").filterNot { it == ""/*||it.length<3*/}
    val mutableListOfWords = MutableList(listOfWords.size) {
        listOfWords[it]
    }

    val wordsMappedToFrequency = HashMap<String, Int?>()
    var firstMapFull = false
    var positionInListOfHashMaps = 0
    //wordsMappedToFrequency = ListOfHashMaps[positionListOfHashMaps]
    mutableListOfWords.forEach { word ->
        if (wordsMappedToFrequency.size != 1073741823) {//if hashmap is not at max_capacity-1 (just for safety)
            if (wordsMappedToFrequency.containsKey(word)) {
                val count = wordsMappedToFrequency[word]!!
                wordsMappedToFrequency[word] = count + 1
            } else {
                wordsMappedToFrequency[word] = 1
            }
        } else{
            println("HASHMAP reached max capacity!!!")
            firstMapFull = true
            //insert code to add a new map to list
            positionInListOfHashMaps++
        }
    }
//    val wordsSorted = mutableListOfWords.sorted().toMutableList()
    val wordsMappedToFrequencySortedByValue = wordsMappedToFrequency.toList().sortedBy { it.second }.reversed().toMap()
    return wordsMappedToFrequencySortedByValue
}
