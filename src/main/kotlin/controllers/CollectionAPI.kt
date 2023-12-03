package controllers

import models.Collection
import persistence.JSONSerializer
import persistence.Serializer
import utils.Utilities.formatListString
import java.util.ArrayList

class CollectionAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType

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

    fun searchVinylByName(searchString: String): String {
        return if (numberOfCollections() == 0) "No collections stored"
        else {
            var listOfCollections = ""
            for (collection in collections) {
                for (vinyl in collection.vinyls) {
                    if (vinyl.albumName.contains(searchString, ignoreCase = true)) {
                        listOfCollections += "${collection.collectionId}: ${collection.collectionName} \n\t${vinyl}\n"
                    }
                }
            }
            if (listOfCollections == "") "No vinyls found for: $searchString"
            else listOfCollections
        }
    }

    fun searchVinylByArtist(searchString: String): String {
        return if (numberOfCollections() == 0) "No collections stored"
        else {
            var listOfCollections = ""
            for (collection in collections) {
                for (vinyl in collection.vinyls) {
                    if (vinyl.artist.contains(searchString, ignoreCase = true)) {
                        listOfCollections += "${collection.collectionId}: ${collection.collectionName} \n\t${vinyl}\n"
                    }
                }
            }
            if (listOfCollections == "") "No vinyls found for: $searchString"
            else listOfCollections
        }
    }

    fun searchVinylByGenre(searchString: String): String {
        return if (numberOfCollections() == 0) "No collections stored"
        else {
            var listOfCollections = ""
            for (collection in collections) {
                for (vinyl in collection.vinyls) {
                    if (vinyl.genre.contains(searchString, ignoreCase = true)) {
                        listOfCollections += "${collection.collectionId}: ${collection.collectionName} \n\t${vinyl}\n"
                    }
                }
            }
            if (listOfCollections == "") "No vinyls found for: $searchString"
            else listOfCollections
        }
    }

//    fun searchVinylBySize(searchInt: Int): String {
//        return if (numberOfCollections() == 0) "No collections stored"
//        else {
//            var listOfCollections = ""
//            for (collection in collections) {
//                for (vinyl in collection.vinyls) {
//                    if (vinyl.sizeInches.contains(searchInt, ignoreCase = true)) {
//                        listOfCollections += "${collection.collectionId}: ${collection.collectionName} \n\t${vinyl}\n"
//                    }
//                }
//            }
//            if (listOfCollections == "") "No vinyls found for: $searchInt"
//            else listOfCollections
//        }
//    }

    fun searchVinylByColour(searchString: String): String {
        return if (numberOfCollections() == 0) "No collections stored"
        else {
            var listOfCollections = ""
            for (collection in collections) {
                for (vinyl in collection.vinyls) {
                    if (vinyl.colour.contains(searchString, ignoreCase = true)) {
                        listOfCollections += "${collection.collectionId}: ${collection.collectionName} \n\t${vinyl}\n"
                    }
                }
            }
            if (listOfCollections == "") "No vinyls found for: $searchString"
            else listOfCollections
        }
    }

    @Throws(Exception::class)
    fun load() {
        collections = serializer.read() as ArrayList<Collection>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(collections)
    }
}

