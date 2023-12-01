package controllers

import models.Collection
import utils.Utilities.formatListString
import java.util.ArrayList

class CollectionAPI() {

    private var collections = ArrayList<Collection>()

    private var lastId = 0
    private fun getId() = lastId++

    fun add(collection: Collection): Boolean {
        collection.collectionId = getId()
        return collections.add(collection)
    }

    fun delete(id: Int) = collections.removeIf { collection -> collection.collectionId == id }

    fun update(id: Int, collection: Collection?): Boolean {

        val foundCollection = findCollection(id)

        if ((foundCollection != null) && (collection != null)) {
            foundCollection.collectionName = collection.collectionName
            return true
        }

        return false
    }

    fun archiveCollection(id: Int): Boolean {
        val foundCollection = findCollection(id)
        if (( foundCollection != null) && (!foundCollection.isCollectionArchived)) {
            foundCollection.isCollectionArchived = true
            return true
        }
        return false
    }

    fun listAllCollections() =
        if (collections.isEmpty()) "No collections stored"
        else formatListString(collections)

    fun listActiveCollections() =
        if (numberOfActiveCollections() == 0) "No active collections stored"
        else formatListString(collections.filter { collection -> !collection.isCollectionArchived })

    fun listArchivedCollections() =
        if (numberOfArchivedCollections() == 0) "No archived collections stored"
        else formatListString(collections.filter { collection -> collection.isCollectionArchived })

    fun numberOfCollections() = collections.size
    fun numberOfArchivedCollections(): Int = collections.count { collection: Collection -> collection.isCollectionArchived }
    fun numberOfActiveCollections(): Int = collections.count { collection: Collection -> !collection.isCollectionArchived }


    fun findCollection(collectionId : Int) =  collections.find{ collection -> collection.collectionId == collectionId }

    fun searchCollectionByTitle(searchString: String) =
        formatListString(
            collections.filter { collection -> collection.collectionName.contains(searchString, ignoreCase = true) }
        )

    fun searchVinylByContents(searchString: String): String {
        return if (numberOfCollections() == 0) "No collections stored"
        else {
            var listOfCollections = ""
            for (collection in collections) {
                for (vinyl in collection.vinyls) {
                    if (vinyl.vinylContents.contains(searchString, ignoreCase = true)) {
                        listOfCollections += "${collection.collectionId}: ${collection.collectionName} \n\t${vinyl}\n"
                    }
                }
            }
            if (listOfCollections == "") "No items found for: $searchString"
            else listOfCollections
        }
    }
}
