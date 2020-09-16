import java.io.File
import kotlin.system.measureNanoTime

val listOfDelimiters = mutableListOf<Char>(' ', '.', ',', ';', ':', '\'', '\'', '!', '?', '<', '>', '/', '\\', '%', '$', '#', '@', '^', '&', '*', '(', ')', '-', '+', '=', '{', '[', ']', '}', '`', '~', '_', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

val listOfEnglishLettersAndForbiddenCharacters = listOfDelimiters + ('A'..'Z') + ('a'..'z')
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
        if(flattenedString.length!=Integer.MAX_VALUE - 9) flattenedString.append(it) else{
                stringBuilderCounter++
                mutableListOfStringBuilders.add(StringBuilder())
                flattenedString = mutableListOfStringBuilders[stringBuilderCounter]
                flattenedString.append(it)
            }
            flattenedString.append(it)
        }
        val wordsMappedToFrequencySortedByValue = getSortedFrequencyMap(/*string*//*flattenedString.toString()*/mutableListOfStringBuilders)
//        println(filesInFolder[mutableListOfStrings.indexOf(flattenedString/*string*/)])
        println()
        wordsMappedToFrequencySortedByValue.forEach { print("${it.key.prependIndent("\n")}=${it.value}") }
//    wordsMappedToFrequencySortedByValue.forEach { print("${it.key.prependIndent("\n")}=${it.value}") }
//    println()

    }
    println(timeToComplete)
}

private fun getSortedFrequencyMap(/*text: String*/listOfStringBuilders: MutableList<StringBuilder>): Map<String, Int?> {
    val listOfWords = text.split(" ", ".", ",", ";", ":", /*"\"",*/ "\'", "!", "?", "<", ">", "/", "\\", "%", "$", "#", "@", "^", "&", "*", "(", ")", "-", "+", "=", "{", "[", "]", "}", "`", "~", "_", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0").filterNot { it == ""/*||it.length<3*/ || it.any { letter -> letter in listOfEnglishLettersAndForbiddenCharacters } }
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
private fun getSortedFrequencyMapOfThreeMostCommon(text: String): Map<String, Int?> {
    val listOfWords = text.split(" ", ".", ",", ";", ":", /*"\"",*/ "\'", "!", "?", "<", ">", "/", "\\", "%", "$", "#", "@", "^", "&", "*", "(", ")", "-", "+", "=", "{", "[", "]", "}", "`", "~", "_", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0").filterNot { it == ""/*||it.length<3*/ || it.any { letter -> letter in listOfEnglishLettersAndForbiddenCharacters } }
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
    var counter = 0

    val wordsMappedToFrequencySortedByValue = wordsMappedToFrequency.toList().sortedBy { it.second }.reversed().toMutableList().takeWhile{
counter++
        counter<7
    }.toMap()
    println((wordsMappedToFrequencySortedByValue.getOrDefault("לא",0)?.div((wordsMappedToFrequencySortedByValue.getOrDefault("אלא",0)!!.toDouble()))))
    return wordsMappedToFrequencySortedByValue
}
