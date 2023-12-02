import controllers.CollectionAPI
import models.Vinyl
import models.Collection
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import kotlin.system.exitProcess

private val collectionAPI = CollectionAPI()

//TODO (Issue 3) Add YAML Serializer
//TODO (Issue 5) Correct CollectionAPITest.kt
//TODO (Issue 6) Fix UI and add nice colours
fun main() = runMenu()

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addCollection()
            2 -> listCollections()
            3 -> updateCollection()
            4 -> deleteCollection()
            5 -> archiveCollection()
            6 -> addVinylToCollection()
            7 -> updateVinylContentsInCollection()
            8 -> deleteAVinyl()
            10 -> searchCollections()
            15 -> searchVinylByName()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
    """ 
         > -----------------------------------------------------  
         > |              Vinyl Collection App                 |
         > -----------------------------------------------------  
         > | COLLECTION MENU                                   |
         > |   1) Add a collection                             |
         > |   2) List collections                             |
         > |   3) Update a collection                          |
         > |   4) Delete a collection                          |
         > |   5) Archive a collection                         |
         > -----------------------------------------------------  
         > | VINYL MENU                                        | 
         > |   6) Add vinyl to a collection                    |
         > |   7) Update vinyl contents on a collection        |
         > |   8) Delete vinyl from a collection               |
         > -----------------------------------------------------  
         > | REPORT MENU FOR COLLECTION                        | 
         > |   10) Search for all notes (by note title)        |
         > |   11) .....                                       |
         > |   12) .....                                       |
         > |   13) .....                                       |
         > |   14) .....                                       |
         > -----------------------------------------------------  
         > | REPORT MENU FOR ITEMS                             |                                
         > |   15) Search for all items (by item description)  |
         > |   16) List TODO Items                             |
         > |   17) .....                                       |
         > |   18) .....                                       |
         > |   19) .....                                       |
         > -----------------------------------------------------  
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)

//------------------------------------
//NOTE MENU
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
            """
                  > -------------------------------------
                  > |   1) View ALL collections         |
                  > |   2) View ACTIVE collections      |
                  > |   3) View ARCHIVED collections    |
                  > -------------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllCollections()
            2 -> listActiveCollections()
            3 -> listArchivedCollections()
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
        // only ask the user to choose the note if notes exist
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
        // only ask the user to choose the note to delete if notes exist
        val id = readNextInt("Enter the id of the collection to delete: ")
        // pass the index of the note to NoteAPI for deleting and check for success.
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
        // only ask the user to choose the note to archive if active notes exist
        val id = readNextInt("Enter the id of the collection to archive: ")
        // pass the index of the note to NoteAPI for archiving and check for success.
        if (collectionAPI.archiveCollection(id)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

//-------------------------------------------
//ITEM MENU (only available for active notes)
//-------------------------------------------
private fun addVinylToCollection() {
    val collection: Collection? = askUserToChooseActiveCollection()
    if (collection != null) {
        if (collection.addVinyl(Vinyl(
                albumName = readNextLine("\t Album Name: "),
                artist = readNextLine("\t Artist: "),
                genre = readNextLine("\t Genre: "),
                sizeInches = readNextInt("\t Size in inches (7inch, 10inch, 12inch): "),
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
            val newSize = readNextInt("Enter new size in inches (7inch, 10inch, 12inch): ")
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
//NOTE REPORTS MENU
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
//ITEM REPORTS MENU
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
//TODO (Issue 4) Add searchVinylByArtist, searchVinylByGenre, searchVinylBySize, searchVinylByColour

//------------------------------------
// Exit App
//------------------------------------
fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}

//------------------------------------
//HELPER FUNCTIONS
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