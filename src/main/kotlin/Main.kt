import java.io.File
import kotlin.system.measureNanoTime

private val listOfDelimiters = mutableListOf<Char>(' ', '.', ',', ';', ':', '\'', '\'', '!', '?', '<', '>', '/', '\\', '%', '$', '#', '@', '^', '&', '*', '(', ')', '-', '+', '=', '{', '[', ']', '}', '`', '~', '_', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

private val listOfEnglishLettersAndForbiddenCharacters = listOfDelimiters + ('A'..'Z') + ('a'..'z')
fun main() {
    val timeToComplete = measureNanoTime {
        val numberOfLettersInAlphabet = 22//22 letters in aleph-beis
        val mutableListOfStrings = getMutableListOfStrings(File("C:\\Users\\Shmuel\\OneDrive\\IdeaProjects\\FindMostCommonSefariaWords\\Tosafot"))
        //restrict any entries with forbidden characters:
        mutableListOfStrings.removeIf { it1 -> it1.none { it2 -> it2 in listOfEnglishLettersAndForbiddenCharacters } }

        val mutableListOfStringBuilders = getMutableListOfStringBuilders(mutableListOfStrings)

        val listOfMapsOfWordsMappedToFrequency = getListOfFrequencyMapsFromListOfStringBuilders(/*string*//*flattenedString.toString()*/mutableListOfStringBuilders)
//        println(filesInFolder[mutableListOfStrings.indexOf(flattenedString/*string*/)])
//        println(wordsMappedToFrequencySortedByValue)

        val mutableListOfWordMapsGroupedByLetter = mutableListOf<MutableMap<String, Int?>>()
        for (i in 1..numberOfLettersInAlphabet) mutableListOfWordMapsGroupedByLetter.add(mutableMapOf())

        val alphabetList = listOf('א', 'ב', 'ג', 'ד', 'ה', 'ו', 'ז', 'ח', 'ט', 'י', 'כ', 'ל', 'מ', 'נ', 'ס', 'ע', 'פ', 'צ', 'ק', 'ר', 'ש', 'ת')
        var indexOfAlephBeisList = 0
        for (frequencyMap in listOfMapsOfWordsMappedToFrequency) {
            for ((word, count) in frequencyMap) {
                while (indexOfAlephBeisList < alphabetList.size) {
                    if (word.first() == alphabetList[indexOfAlephBeisList]) {
                        for (mutableMap in mutableListOfWordMapsGroupedByLetter) {
                            if(word in mutableMap){
                                if (count != null) {
                                    mutableMap[word] = mutableMap[word]?.plus(count)
                                //TODO at this point we realized that arrays were faster. goodby forever. hopefully.
                                }
                            }
                        }
                        println("do something")
                    }
                    indexOfAlephBeisList++
                }

            }
        }

        listOfMapsOfWordsMappedToFrequency.forEach { map ->
            map.forEach { entry ->
                println("${entry.key.prependIndent("\n")}=${entry.value}")
            }
        }
//    wordsMappedToFrequencySortedByValue.forEach { print("${it.key.prependIndent("\n")}=${it.value}") }
//    println()
        //remove cross-map duplicates so that each map contains unique words
        for (map1: MutableMap<String, Int?> in listOfMapsOfWordsMappedToFrequency) {
            for (map2: MutableMap<String, Int?> in listOfMapsOfWordsMappedToFrequency.subList(listOfMapsOfWordsMappedToFrequency.indexOf(map1) + 1, listOfMapsOfWordsMappedToFrequency.size)) {
                for ((key, value) in map2.toMap()) {
                    if (key in map1) {
                        if (value != null) {
                            map1[key] = map1[key]?.plus(value)
                            map2.remove(key)
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

private fun getMutableListOfStringBuilders(mutableListOfStrings: MutableList<String>): MutableList<StringBuilder> {
    var stringBuilderCounter = 0
    val mutableListOfStringBuilders = mutableListOf(StringBuilder())
    var flattenedString = mutableListOfStringBuilders[stringBuilderCounter]
    mutableListOfStrings.forEach {
        if (flattenedString.length + it.length >= 33_000) flattenedString.append(it) else {
            stringBuilderCounter++
            mutableListOfStringBuilders.add(StringBuilder())
            flattenedString = mutableListOfStringBuilders[stringBuilderCounter]
            flattenedString.append(it)
        }
    }
    return mutableListOfStringBuilders
}

private fun getMutableListOfStrings(folder: File): MutableList<String> {
//        val folder = File("C:\\Users\\Shmuel\\OneDrive\\Desktop\\Sefaria Texts (Excluding Tanach, Talmud, and Tanaitic texts)\\Relevant categories")
    val filesInFolder = folder.walk().filter { it.name == "merged.txt" }.toList()
    val mutableListOfStrings = mutableListOf<String>()
    for (file in filesInFolder) {
        val bufferedReader = file.bufferedReader()
        mutableListOfStrings.add(bufferedReader.readText())
        bufferedReader.close()
    }
    return mutableListOfStrings
}

private fun getListOfFrequencyMapsFromListOfStringBuilders(/*text: String*/listOfStringBuilders: MutableList<StringBuilder>): MutableList<MutableMap<String, Int?>> {
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
            newList.add(hashMap.toList()/*.sortedBy { pair -> pair.second }.reversed()*/.toMap().toMutableMap())
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

fun Map<String, Int?>.organizeByLetter() {
    val x = 5
    val new = x + 1
}

fun addOneToNumber(int: Int): Int {
    return int + 1
}