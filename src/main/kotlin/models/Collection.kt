package models

import utils.Utilities

data class Collection ( var collectionId: Int = 0,
                        var collectionName: String = "",
                        var isCollectionArchived: Boolean = false,
                        var vinyls : MutableSet<Vinyl> = mutableSetOf())
{

    private var lastVinylId = 0
    private fun getVinylId() = lastVinylId++

    fun addVinyl(vinyl: Vinyl) : Boolean {
        vinyl.vinylId = getVinylId()
        return vinyls.add(vinyl)
    }

    fun numberOfVinyls() = vinyls.size

    fun findOne(id: Int): Vinyl?{
        return vinyls.find{ vinyl -> vinyl.vinylId == id }
    }

    fun delete(id: Int): Boolean {
        return vinyls.removeIf { vinyl -> vinyl.vinylId == id}
    }

    fun update(id: Int, newVinyl : Vinyl): Boolean {
        val foundVinyl = findOne(id)

        if (foundVinyl != null){
            foundVinyl.albumName = newVinyl.albumName
            foundVinyl.artist = newVinyl.artist
            foundVinyl.genre = newVinyl.genre
            foundVinyl.sizeInches = newVinyl.sizeInches
            foundVinyl.colour = newVinyl.colour
            return true
        }

        return false
    }

    fun listVinyls() =
        if (vinyls.isEmpty())  "\tNO VINYLS ADDED"
        else  Utilities.formatSetString(vinyls)

    override fun toString(): String {
        val archived = if (isCollectionArchived) 'Y' else 'N'
        return "$collectionId: $collectionName, Archived($archived) \n${listVinyls()}"
    }

}