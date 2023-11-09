package com.example.vibeslocal.models

interface IGroupingDescriptionStrategy {
    fun getDescription(): String
}

class PlaylistDescriptionStrategy : IGroupingDescriptionStrategy{
    override fun getDescription(): String = "Playlist test"
}

class AlbumsDescriptionStrategy : IGroupingDescriptionStrategy{
    override fun getDescription(): String = "Albums test"
}

class ArtistsDescriptionStrategy : IGroupingDescriptionStrategy{
    override fun getDescription(): String = "Artists test"
}

class GenresDescriptionStrategy : IGroupingDescriptionStrategy{
    override fun getDescription(): String = "Genres test"
}