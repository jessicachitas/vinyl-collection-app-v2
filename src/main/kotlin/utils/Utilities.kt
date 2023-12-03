package utils

import models.Vinyl
import models.Collection

object Utilities {

    @JvmStatic
    fun formatListString(collectionsToFormat: List<Collection>): String =
        collectionsToFormat
            .joinToString(separator = "\n") { collection ->  "$collection" }

    @JvmStatic
    fun formatSetString(vinylsToFormat: Set<Vinyl>): String =
        vinylsToFormat
            .joinToString(separator = "\n") { vinyl ->  "\t$vinyl" }

}