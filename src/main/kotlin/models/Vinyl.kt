package models

data class Vinyl( var vinylId: Int = 0,
                 var albumName: String = "",
                 var artist: String = "",
                 var genre: String = "",
                 var sizeInches: Int,
                 var colour : String = "",
                 var isVinylArchived: Boolean = false)