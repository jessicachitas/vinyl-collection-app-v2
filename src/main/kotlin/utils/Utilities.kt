package utils

import models.Vinyl
import models.Collection

object Utilities {

    // NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(collectionsToFormat: List<Collection>): String =
        collectionsToFormat
            .joinToString(separator = "\n") { collection ->  "$collection" }

    @JvmStatic
    fun formatSetString(vinylsToFormat: Set<Vinyl>): String =
        vinylsToFormat
            .joinToString(separator = "\n") { vinyl ->  "\t$vinyl" }

}