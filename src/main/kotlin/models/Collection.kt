package models

data class Collection ( var collectionId: Int = 0,
                        var collectionName: String = "",
                        var isCollectionArchived: Boolean = false,
                        var vinyls : MutableSet<Vinyl> = mutableSetOf())