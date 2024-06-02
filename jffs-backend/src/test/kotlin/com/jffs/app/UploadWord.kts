import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient

fun getAttribute(attributeName: String): List<String> {
    print("Input $attributeName (terminated by -1): ")
    val attributes: MutableList<String> = mutableListOf()
    do {
        val attribute = readLine();

        when {
            "-1" != attribute -> {
                attributes.add(attribute!!)
            }
        }

    } while ("-1" != attribute)

    return attributes.toList()
}

data class Word(
    val word: String,
    val meanings: List<Meaning>
)

data class Meaning(
    val definition: String,
    val synonyms: List<String> = listOf(),
    val examples: List<String> = listOf()
)

val words: MutableList<Word> = mutableListOf();

do {
    print("Input word: ")
    val word = readLine()
    print("Input Meaning: ")
    val definition = readLine()
    val synonyms = getAttribute("Synonyms")
    val examples = getAttribute("Examples")

    val meanings: MutableList<Meaning> = mutableListOf()
    meanings.add(Meaning(definition!!, synonyms, examples))

    words.add(Word(word!!, meanings))

    print("Any more words? (Y/y/n/N): ")
    val option = readLine()
} while (option == "Y" || option == "y")

//println (words)
val connectionString =
    "mongodb+srv://<user>:<password>@prod.0nv9qyd.mongodb.net/?retryWrites=true&w=majority&appName=<databaseName>"
val serverApi = ServerApi.builder()
    .version(ServerApiVersion.V1)
    .build()
val mongoClientSettings = MongoClientSettings.builder()
    .applyConnectionString(ConnectionString(connectionString))
    .serverApi(serverApi)
    .build()
val mongoClient = MongoClient.Factory.create(mongoClientSettings);

val database = mongoClient.getDatabase("vocab");
val collection = database.getCollection<Word>("word")

words.forEach { word ->
    kotlinx.coroutines.runBlocking {
        go(word)
    }
}

suspend fun kotlinx.coroutines.CoroutineScope.go(word : Word) {
    println ("Adding " + word)
    collection.insertOne(word)
}
