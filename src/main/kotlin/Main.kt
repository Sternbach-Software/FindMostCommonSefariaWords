import java.io.File
import kotlin.system.measureNanoTime

private val listOfDelimiters = mutableListOf<Char>(' ', '.', ',', ';', ':', '\'', '\'', '!', '?', '<', '>', '/', '\\', '%', '$', '#', '@', '^', '&', '*', '(', ')', '-', '+', '=', '{', '[', ']', '}', '`', '~', '_', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

private val listOfEnglishLettersAndForbiddenCharacters = listOfDelimiters + ('A'..'Z') + ('a'..'z')
fun main() {
    val timeToComplete = measureNanoTime {
        val folder = File("C:\\Users\\Shmuel\\OneDrive\\IdeaProjects\\FindMostCommonSefariaWords\\Tosafot")
//        val folder = File("C:\\Users\\Shmuel\\OneDrive\\Desktop\\Sefaria Texts (Excluding Tanach, Talmud, and Tanaitic texts)\\Relevant categories")
        val filesInFolder = folder.walk().filter { it.name == "merged.txt" }.toList()
        val mutableListOfStrings = mutableListOf<String>()
        for (file in filesInFolder) {
            val bufferedReader = file.bufferedReader()
            mutableListOfStrings.add(bufferedReader.readText())
            bufferedReader.close()
        }
        /*stringBuilder.filter {
        it in listOfEnglishLettersAndForbiddenCharacters
    }*/
/*    val bufferedReader = File("C:\\Users\\Shmuel\\OneDrive\\Desktop\\Sefaria Texts (Excluding Tanach, Talmud, and Tanaitic texts)\\Relevant categories\\Halakhah\\Shulchan Arukh\\Shulchan Arukh, Orach Chayim\\Hebrew\\merged.txt").bufferedReader()
    val bookText = bufferedReader.readText()
    val stringBuilder1 = StringBuilder(bookText)*/
//            text.removeRange(0, "(?<=\n)[\\p{InHebrew}\\s.,;:\'\"!?</>\\\\]+(?=\n)".toRegex().find(text)?.value?.indices?.first?:0)

        /*  stringBuilder1.filter {
        it in listOfEnglishLettersAndForbiddenCharacters
    }*/
//    val text = "hello hello hello hello hello hello hello hello one one one one a b n a a a a a a a a a a a a a a a a a ab b b b b a a a a"
        mutableListOfStrings.removeIf { it1 -> it1.none { it2 -> it2 in listOfEnglishLettersAndForbiddenCharacters } }
//    for(string in mutableListOfStrings){
        var stringBuilderCounter = 0
        val mutableListOfStringBuilders = mutableListOf(StringBuilder())
        var flattenedString = mutableListOfStringBuilders[stringBuilderCounter]
//        var flattenedString = StringBuilder()
        mutableListOfStrings.forEach {
            if (flattenedString.length + it.length >= 33_000) flattenedString.append(it) else {
                stringBuilderCounter++
                mutableListOfStringBuilders.add(StringBuilder())
                flattenedString = mutableListOfStringBuilders[stringBuilderCounter]
                flattenedString.append(it)
            }
        }

        val listOfMapsOfWordsMappedToFrequencySortedByValue = getListOfSortedFrequencyMapsFromListOfStringBuilders(/*string*//*flattenedString.toString()*/mutableListOfStringBuilders)
//        println(filesInFolder[mutableListOfStrings.indexOf(flattenedString/*string*/)])
//        println(wordsMappedToFrequencySortedByValue)
        val textFile = File("output.txt")
        textFile.createNewFile()
        val bw = textFile.bufferedWriter()
        listOfMapsOfWordsMappedToFrequencySortedByValue.forEach { map -> map.forEach { entry -> bw.append("${entry.key.prependIndent("\n")}=${entry.value}") } }
//    wordsMappedToFrequencySortedByValue.forEach { print("${it.key.prependIndent("\n")}=${it.value}") }
//    println()
        bw.close()
        val listOfCondensedFrequencyMaps = mutableListOf<Map<String, Int>>()
        //remove cross-map duplicates so that each map contains unique words
        //results in a situation where you will potentially have the same amount of maps as the beggining, but each map will (only contain words which are in no other map and) just contain a few words. The next step is to fill the maps sequentially until you get to the last map. values to
        for (map1: MutableMap<String, Int?> in listOfMapsOfWordsMappedToFrequencySortedByValue) {
            for (map2: MutableMap<String, Int?> in listOfMapsOfWordsMappedToFrequencySortedByValue.subList(listOfMapsOfWordsMappedToFrequencySortedByValue.indexOf(map1),listOfMapsOfWordsMappedToFrequencySortedByValue.size)) {
                for ((key, value) in map2.toMap()) {
                    if (key in map1) {
                        if (value != null) {
                            map1[key] = map1[key]?.plus(value)
                            map2.remove(key)
                            //add key's associated value (its word count) as recorded in map2 to key's value in map1
                            //delete/remove key from map2
                        }
                    }
                }
            }
        }
        /*   println("Number of accumulated HashMaps: ${hashMapCounter+1}")
            hashMapCounter++
            mutableListOfMaps.add(HashMap<String, Int?>())
            wordsMappedToFrequency = mutableListOfMaps[hashMapCounter]
            wordsMappedToFrequency.incrementCountOrSetToOne(word)
        }*/
    }
    println("Seconds to complete: ${timeToComplete / 1_000_000_000}")
}

private fun getListOfSortedFrequencyMapsFromListOfStringBuilders(/*text: String*/listOfStringBuilders: MutableList<StringBuilder>): MutableList<MutableMap<String, Int?>> {
    val mutableListOfMaps = mutableListOf(HashMap<String, Int?>())
    val newList = mutableListOf<MutableMap<String, Int?>>()
    for (stringBuilder in listOfStringBuilders) {
        val text = stringBuilder.toString()

        val listOfWords = text.split(" ", ".", ",", ";", ":", /*"\"",*/ "\'", "!", "?", "<", ">", "/", "\\", "%", "$", "#", "@", "^", "&", "*", "(", ")", "-", "+", "=", "{", "[", "]", "}", "`", "~", "_", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0").filterNot { it == ""/*||it.length<3*/ || it.any { letter -> letter in listOfEnglishLettersAndForbiddenCharacters } }
        val mutableListOfWords = MutableList(listOfWords.size) {
            listOfWords[it]
        }
        var hashMapCounter = 0
        var wordsMappedToFrequency = mutableListOfMaps[hashMapCounter]
        //wordsMappedToFrequency = ListOfHashMaps[positionListOfHashMaps]
        mutableListOfWords.forEach { word ->
            if (wordsMappedToFrequency.size != 33_000/*1073741823*/) {//if hashmap is not at max_capacity-1 (just for safety)
                wordsMappedToFrequency.incrementCountOrSetToOne(word)
            } else {
                println("Number of accumulated HashMaps: ${hashMapCounter + 1}")
                hashMapCounter++
                mutableListOfMaps.add(HashMap<String, Int?>())
                wordsMappedToFrequency = mutableListOfMaps[hashMapCounter]
                wordsMappedToFrequency.incrementCountOrSetToOne(word)
            }
        }
//    val wordsSorted = mutableListOfWords.sorted().toMutableList()
        mutableListOfMaps.forEach { hashMap ->
            newList.add(hashMap.toList().sortedBy { pair -> pair.second }.reversed().toMap().toMutableMap())
        }
//    val wordsMappedToFrequencySortedByValue = wordsMappedToFrequency.toList().sortedBy { it.second }.reversed().toMap()
//    return wordsMappedToFrequencySortedByValue
    }
    return newList
}

private fun getSortedFrequencyMapOfThreeMostCommon(text: String): Map<String, Int?> {
    val listOfWords = text.split(" ", ".", ",", ";", ":", /*"\"",*/ "\'", "!", "?", "<", ">", "/", "\\", "%", "$", "#", "@", "^", "&", "*", "(", ")", "-", "+", "=", "{", "[", "]", "}", "`", "~", "_", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0").filterNot { it == ""/*||it.length<3*/ || it.any { letter -> letter in listOfEnglishLettersAndForbiddenCharacters } }
    val mutableListOfWords = MutableList(listOfWords.size) {
        listOfWords[it]
    }

    val wordsMappedToFrequency = HashMap<String, Int?>()
    var positionInListOfHashMaps = 0
    //wordsMappedToFrequency = ListOfHashMaps[positionListOfHashMaps]
    mutableListOfWords.forEach { word ->
        if (wordsMappedToFrequency.size != 1073741823) {//if hashmap is not at max_capacity-1 (just for safety)
            wordsMappedToFrequency.incrementCountOrSetToOne(word)
        } else {
            println("HASHMAP reached max capacity!!!")
//TODO update this with other code if you want to use this function again.
            //insert code to add a new map to list
            positionInListOfHashMaps++
        }
    }
//    val wordsSorted = mutableListOfWords.sorted().toMutableList()
    var counter = 0

    val wordsMappedToFrequencySortedByValue = wordsMappedToFrequency.toList().sortedBy { it.second }.reversed().toMutableList().takeWhile {
        counter++
        counter < 7
    }.toMap()
    println((wordsMappedToFrequencySortedByValue.getOrDefault("לא", 0)?.div((wordsMappedToFrequencySortedByValue.getOrDefault("אלא", 0)!!.toDouble()))))
    return wordsMappedToFrequencySortedByValue
}

private fun HashMap<String, Int?>.incrementCountOrSetToOne(word: String) = if (containsKey(word)) this[word] = this[word]?.plus(1) else this[word] = 1
