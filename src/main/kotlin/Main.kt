import controllers.CollectionAPI
import models.Vinyl
import models.Collection
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import kotlin.system.exitProcess
import persistence.YamlSerializer
import java.io.File

private val collectionAPI = CollectionAPI(YamlSerializer(File("collection.yaml")))

const val colourReset = "\u001b[0m"
const val magenta = "\u001b[35m"
const val cyan = "\u001b[36m"

fun main() = runMenu()

//--------------
// Menu UIs
//--------------

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> collectionMenu()
            2 -> vinylMenu()
//            1 -> addCollection()
//            2 -> listCollections()
//            3 -> updateCollection()
//            4 -> deleteCollection()
//            5 -> archiveCollection()
//            6 -> addVinylToCollection()
//            7 -> updateVinylContentsInCollection()
//            8 -> deleteAVinyl()
//            10 -> searchCollections()
//            15 -> searchVinylByName()
//            16 -> searchVinylByArtist()
//            17 -> searchVinylByGenre()
//            18 -> searchVinylByColour()
            98 -> save()
            99 -> load()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
    """ $magenta
         > -----------------------------------------------------  
         > |           ┓ ┏┓     ┓     ┏┓  •    •  ┏┓           |
         > |           ┃┃┃┣┓┏┓╋┏┣┓┏┓  ┗┓┏┓┓┏┓┏┓┓┏┓┏┛           |
         > |           ┗┻┛┛┗┗┻┗┗┛┗┗┻  ┗┛┣┛┗┛┗┛┗┗┛┗•            |
         > |                            ┛                      |
         > ----------------------------------------------------- $colourReset
         > |                   $cyan MAIN MENU $colourReset                     | 
         > | $magenta  1)$colourReset Collection Menu                              |
         > | $magenta  2)$colourReset Vinyl Menu                                   |
         > -----------------------------------------------------  
         > | $cyan  98)$colourReset Save                                        |
         > | $cyan  99)$colourReset Load                                        |
         > | $magenta  0)$colourReset Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)

//fun mainMenu() = readNextInt(
//    """
//         > -----------------------------------------------------
//         > |              Vinyl Collection App                 |
//         > -----------------------------------------------------
//         > | COLLECTION MENU                                   |
//         > |   1) Add a collection                             |
//         > |   2) List collections                             |
//         > |   3) Update a collection                          |
//         > |   4) Delete a collection                          |
//         > |   5) Archive a collection                         |
//         > -----------------------------------------------------
//         > | VINYL MENU                                        |
//         > |   6) Add vinyl to a collection                    |
//         > |   7) Update vinyl contents on a collection        |
//         > |   8) Delete vinyl from a collection               |
//         > -----------------------------------------------------
//         > | REPORT MENU FOR COLLECTION                        |
//         > |   10) Search for collection by title              |
//         > -----------------------------------------------------
//         > | REPORT MENU FOR ITEMS                             |
//         > |   15) Search for a vinyl by name                  |
//         > |   16) Search for a vinyl by artist                |
//         > |   17) Search for a vinyl by genre                 |
//         > |   18) Search for a vinyl by colour                |
//         > -----------------------------------------------------
//         > |   98) Save                                        |
//         > |   99) Load                                        |
//         > |   0) Exit                                         |
//         > -----------------------------------------------------
//         > ==>> """.trimMargin(">")
//)

fun collectionMenu() {
    val option = readNextInt(
        """$magenta
         > -----------------------------------------------------  
         > |           ┓ ┏┓     ┓     ┏┓  •    •  ┏┓           |
         > |           ┃┃┃┣┓┏┓╋┏┣┓┏┓  ┗┓┏┓┓┏┓┏┓┓┏┓┏┛           |
         > |           ┗┻┛┛┗┗┻┗┗┛┗┗┻  ┗┛┣┛┗┛┗┛┗┗┛┗•            |
         > |                            ┛                      |
         > ----------------------------------------------------- $colourReset  
         > |                $cyan COLLECTION MENU $colourReset                  |
         > |  $magenta 1)$colourReset Add a collection                             |
         > |  $magenta 2)$colourReset List collections                             |
         > |  $magenta 3)$colourReset Update a collection                          |
         > |  $magenta 4)$colourReset Delete a collection                          |
         > |  $magenta 5)$colourReset Archive a collection                         |
         > -----------------------------------------------------  
         > |           $cyan SEARCH MENU FOR COLLECTION $colourReset            | 
         > |  $magenta 10)$colourReset Search for collection by title              |
         > -----------------------------------------------------
         > |  $cyan 99)$colourReset Back to Main Menu                           |
         > ----------------------------------------------------- 
         > ==>> """.trimMargin(">")
    )

    when (option) {
        1 -> addCollection()
        2 -> listCollections()
        3 -> updateCollection()
        4 -> deleteCollection()
        5 -> archiveCollection()
        10 -> searchCollections()
        99 -> mainMenu()
        else -> println("Invalid option entered: $option")
    }
}

fun vinylMenu() {
    val option = readNextInt(
        """$magenta
         > -----------------------------------------------------  
         > |           ┓ ┏┓     ┓     ┏┓  •    •  ┏┓           |
         > |           ┃┃┃┣┓┏┓╋┏┣┓┏┓  ┗┓┏┓┓┏┓┏┓┓┏┓┏┛           |
         > |           ┗┻┛┛┗┗┻┗┗┛┗┗┻  ┗┛┣┛┗┛┗┛┗┗┛┗•            |
         > |                            ┛                      |
         > ----------------------------------------------------- $colourReset
         > |                   $cyan VINYL MENU $colourReset                    | 
         > |  $magenta 6)$colourReset Add vinyl to a collection                    |
         > |  $magenta 7)$colourReset Update vinyl contents on a collection        |
         > |  $magenta 8)$colourReset Delete vinyl from a collection               |
         > ----------------------------------------------------- 
         > |             $cyan SEARCH MENU FOR VINYLS $colourReset              |                                
         > |  $magenta 15)$colourReset Search for a vinyl by name                  |
         > |  $magenta 16)$colourReset Search for a vinyl by artist                |
         > |  $magenta 17)$colourReset Search for a vinyl by genre                 |
         > |  $magenta 18)$colourReset Search for a vinyl by colour                |
         > -----------------------------------------------------  
         > |  $cyan 99)$colourReset Back to Main Menu                           |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
    )

    when (option) {
        6 -> addVinylToCollection()
        7 -> updateVinylContentsInCollection()
        8 -> deleteAVinyl()
        15 -> searchVinylByName()
        16 -> searchVinylByArtist()
        17 -> searchVinylByGenre()
        18 -> searchVinylByColour()
        99 -> mainMenu()
        else -> println("Invalid option entered: $option")
    }
}

//------------------------------------
//  Collection Menu
//------------------------------------
fun addCollection() {
    val collectionName = readNextLine("Enter a name for the collection: ")
    val isAdded = collectionAPI.add(Collection(collectionName = collectionName))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listCollections() {
    if (collectionAPI.numberOfCollections() > 0) {
        val option = readNextInt(
            """$magenta
            > -----------------------------------------------------  
            > |           ┓ ┏┓     ┓     ┏┓  •    •  ┏┓           |
            > |           ┃┃┃┣┓┏┓╋┏┣┓┏┓  ┗┓┏┓┓┏┓┏┓┓┏┓┏┛           |
            > |           ┗┻┛┛┗┗┻┗┗┛┗┗┻  ┗┛┣┛┗┛┗┛┗┗┛┗•            |
            > |                            ┛                      |
            > ----------------------------------------------------- $colourReset
            > |  $magenta 1)$colourReset View ALL collections                         |
            > |  $magenta 2)$colourReset View ACTIVE collections                      |
            > |  $magenta 3)$colourReset View ARCHIVED collections                    |
            > -----------------------------------------------------
            > |  $cyan 99)$colourReset Back to Collection Menu                     |
            > -----------------------------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllCollections()
            2 -> listActiveCollections()
            3 -> listArchivedCollections()
            99 -> collectionMenu()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No collections stored")
    }
}

fun listAllCollections() = println(collectionAPI.listAllCollections())
fun listActiveCollections() = println(collectionAPI.listActiveCollections())
fun listArchivedCollections() = println(collectionAPI.listArchivedCollections())

fun updateCollection() {
    listCollections()
    if (collectionAPI.numberOfCollections() > 0) {
        val id = readNextInt("Enter the id of the collection to update: ")
        if (collectionAPI.findCollection(id) != null) {
            val collectionName = readNextLine("Enter a name for the collection: ")
            if (collectionAPI.update(id, Collection(0, collectionName, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no collections for this index number")
        }
    }
}

fun deleteCollection() {
    listCollections()
    if (collectionAPI.numberOfCollections() > 0) {
        val id = readNextInt("Enter the id of the collection to delete: ")
        val collectionToDelete = collectionAPI.delete(id)
        if (collectionToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun archiveCollection() {
    listActiveCollections()
    if (collectionAPI.numberOfActiveCollections() > 0) {
        val id = readNextInt("Enter the id of the collection to archive: ")
        if (collectionAPI.archiveCollection(id)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

//----------------------------------------------------
//  Vinyl Menu (only available for active collections)
//----------------------------------------------------
private fun addVinylToCollection() {
    val collection: Collection? = askUserToChooseActiveCollection()
    if (collection != null) {
        if (collection.addVinyl(Vinyl(
                albumName = readNextLine("\t Album Name: "),
                artist = readNextLine("\t Artist: "),
                genre = readNextLine("\t Genre: "),
                sizeInches = readNextInt("\t Size in inches (7, 10, 12): "),
                colour = readNextLine("\t Colour: ")
            )))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}

fun updateVinylContentsInCollection() {
    val collection: Collection? = askUserToChooseActiveCollection()
    if (collection != null) {
        val vinyl: Vinyl? = askUserToChooseVinyl(collection)
        if (vinyl != null) {
            val newAlbumName = readNextLine("Enter new album name: ")
            val newArtist = readNextLine("Enter new artist: ")
            val newGenre = readNextLine("Enter new genre: ")
            val newSize = readNextInt("Enter new size in inches (7, 10, 12): ")
            val newColour = readNextLine("Enter new colour: ")
            if (collection.update(vinyl.vinylId, Vinyl(
                    albumName = newAlbumName,
                    artist = newArtist,
                    genre = newGenre,
                    sizeInches = newSize,
                    colour = newColour
            ))) {
                println("Item contents updated")
            } else {
                println("Item contents NOT updated")
            }
        } else {
            println("Invalid Item Id")
        }
    }
}

fun deleteAVinyl() {
    val collection: Collection? = askUserToChooseActiveCollection()
    if (collection != null) {
        val vinyl: Vinyl? = askUserToChooseVinyl(collection)
        if (vinyl != null) {
            val isDeleted = collection.delete(vinyl.vinylId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

//------------------------------------
//  Collection Search Menu
//------------------------------------
fun searchCollections() {
    val searchTitle = readNextLine("Enter the name of collection you want to search: ")
    val searchResults = collectionAPI.searchCollectionByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No collections found")
    } else {
        println(searchResults)
    }
}

//------------------------------------
//  Vinyl Search Menu
//------------------------------------
fun searchVinylByName() {
    val searchContents = readNextLine("Enter the album name to search by: ")
    val searchResults = collectionAPI.searchVinylByName(searchContents)
    if (searchResults.isEmpty()) {
        println("No vinyls found")
    } else {
        println(searchResults)
    }
}

fun searchVinylByArtist() {
    val searchContents = readNextLine("Enter the artist name to search by: ")
    val searchResults = collectionAPI.searchVinylByArtist(searchContents)
    if (searchResults.isEmpty()) {
        println("No vinyls found")
    } else {
        println(searchResults)
    }
}

fun searchVinylByGenre() {
    val searchContents = readNextLine("Enter the genre to search by: ")
    val searchResults = collectionAPI.searchVinylByGenre(searchContents)
    if (searchResults.isEmpty()) {
        println("No vinyls found")
    } else {
        println(searchResults)
    }
}

//fun searchVinylBySize() {
//    val searchContents = readNextInt("Enter the size in inches to search by (7, 10, 12): ")
//    val searchResults = collectionAPI.searchVinylBySize(searchContents)
//    if (searchResults.isEmpty()) {
//        println("No vinyls found")
//    } else {
//        println(searchResults)
//    }
//}

fun searchVinylByColour() {
    val searchContents = readNextLine("Enter the colour to search by: ")
    val searchResults = collectionAPI.searchVinylByColour(searchContents)
    if (searchResults.isEmpty()) {
        println("No vinyls found")
    } else {
        println(searchResults)
    }
}

//------------------------------------
// Save and Load
//------------------------------------

fun save() {
    try {
        collectionAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        collectionAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

//------------------------------------
// Exit App
//------------------------------------

fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}

//------------------------------------
//  Helper Functions
//------------------------------------

private fun askUserToChooseActiveCollection(): Collection? {
    listActiveCollections()
    if (collectionAPI.numberOfActiveCollections() > 0) {
        val collection = collectionAPI.findCollection(readNextInt("\nEnter the id of the collection: "))
        if (collection != null) {
            if (collection.isCollectionArchived) {
                println("Collection is NOT Active, it is Archived")
            } else {
                return collection //chosen note is active
            }
        } else {
            println("Collection id is not valid")
        }
    }
    return null //selected note is not active
}

private fun askUserToChooseVinyl(collection: Collection): Vinyl? {
    if (collection.numberOfVinyls() > 0) {
        print(collection.listVinyls())
        return collection.findOne(readNextInt("\nEnter the id of the vinyl: "))
    }
    else{
        println ("No vinyls for chosen collection")
        return null
    }
}