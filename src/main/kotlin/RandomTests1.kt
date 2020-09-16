import java.io.File
fun main() {
    val listOfFiles = File("C:\\Users\\Shmuel\\OneDrive\\Desktop\\Sefaria Texts (Excluding Tanach, Talmud, and Tanaitic texts)\\Relevant categories").walk()
//    println(listOfFiles.toList())
    val mutableListOfExtensions = mutableListOf<String>()
    listOfFiles.forEach{
//        println(it.name)
        if(it.isDirectory && it.name.contains("English")) /*println(*/it.deleteRecursively()//)
    }
    println(mutableListOfExtensions)
}
