import controllers.CollectionAPI
import models.Vinyl
import models.Collection
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import kotlin.system.exitProcess

private val collectionAPI = CollectionAPI()

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
            8 -> deleteACollection()
            10 -> searchCollections()
            15 -> searchVinyls()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
    """ 
         > -----------------------------------------------------  
         > |                  NOTE KEEPER APP                  |
         > -----------------------------------------------------  
         > | NOTE MENU                                         |
         > |   1) Add a note                                   |
         > |   2) List notes                                   |
         > |   3) Update a note                                |
         > |   4) Delete a note                                |
         > |   5) Archive a note                               |
         > -----------------------------------------------------  
         > | ITEM MENU                                         | 
         > |   6) Add item to a note                           |
         > |   7) Update item contents on a note               |
         > |   8) Delete item from a note                      |
         > -----------------------------------------------------  
         > | REPORT MENU FOR NOTES                             | 
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
    val isAdded = collectionAPI.add(collection(collectionName = collectionName))

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
    val collection: Collection? = askUserToChooseActiveNote()
    if (collection != null) {
        if (collection.addVinyl(Vinyl(albumName = readNextLine("\t Album Name: "), artist = readNextLine()))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}

fun updateItemContentsInNote() {
    val note: Note? = askUserToChooseActiveNote()
    if (note != null) {
        val item: Item? = askUserToChooseItem(note)
        if (item != null) {
            val newContents = readNextLine("Enter new contents: ")
            if (note.update(item.itemId, Item(itemContents = newContents))) {
                println("Item contents updated")
            } else {
                println("Item contents NOT updated")
            }
        } else {
            println("Invalid Item Id")
        }
    }
}

fun deleteAnItem() {
    val note: Note? = askUserToChooseActiveNote()
    if (note != null) {
        val item: Item? = askUserToChooseItem(note)
        if (item != null) {
            val isDeleted = note.delete(item.itemId)
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
fun searchNotes() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = noteAPI.searchNotesByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}

//------------------------------------
//ITEM REPORTS MENU
//------------------------------------
fun searchItems() {
    val searchContents = readNextLine("Enter the item contents to search by: ")
    val searchResults = noteAPI.searchItemByContents(searchContents)
    if (searchResults.isEmpty()) {
        println("No items found")
    } else {
        println(searchResults)
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
//HELPER FUNCTIONS
//------------------------------------

private fun askUserToChooseActiveNote(): Note? {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        val note = noteAPI.findNote(readNextInt("\nEnter the id of the note: "))
        if (note != null) {
            if (note.isNoteArchived) {
                println("Note is NOT Active, it is Archived")
            } else {
                return note //chosen note is active
            }
        } else {
            println("Note id is not valid")
        }
    }
    return null //selected note is not active
}

private fun askUserToChooseItem(note: Note): Item? {
    if (note.numberOfItems() > 0) {
        print(note.listItems())
        return note.findOne(readNextInt("\nEnter the id of the item: "))
    }
    else{
        println ("No items for chosen note")
        return null
    }
}