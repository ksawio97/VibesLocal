package com.example.vibeslocal.models

interface IGroupingDescriptionStrategy {
    fun getDescription(optionModel: OptionModel, songs: Collection<SongModel>): String
}

open class GenericDescriptionStrategy: IGroupingDescriptionStrategy {
    protected fun getTextBasedOnCount(count: Int, ifOne: String, ifMultiple: String): String {
        return if (count == 1) ifOne else ifMultiple
    }

    protected fun joinDataToInfo(data: Collection<String>, postfix: String) = data.joinToString(separator = ", ", limit = 2, postfix = " $postfix")
    protected fun joinDescriptionInfo(info: Collection<String>) = info.joinToString(separator = " | ")
    override fun getDescription(optionModel: OptionModel, songs: Collection<SongModel>): String {
        return "${optionModel.songsCount} ${getTextBasedOnCount(optionModel.songsCount, "Song", "Songs")}"
    }
}
class PlaylistDescriptionStrategy: GenericDescriptionStrategy() {
    //displays songs count and distinct albums
    override fun getDescription(optionModel: OptionModel, songs: Collection<SongModel>): String {
        val songCountInfo = super.getDescription(optionModel, songs)
        val distinctAlbums = songs
            .distinctBy { it.albumId }
            .map { it.albumTitle }
        return joinDescriptionInfo(
            listOf(
                songCountInfo,
                joinDataToInfo(distinctAlbums, getTextBasedOnCount(distinctAlbums.size, "Album", "Albums"))
            )
        )
    }
}


class AlbumsDescriptionStrategy: GenericDescriptionStrategy() {
    //displays songs count and distinct genres
    override fun getDescription(optionModel: OptionModel, songs: Collection<SongModel>): String {
        val songCountInfo = super.getDescription(optionModel, songs)
        val distinctGenres = songs
            .map { it.genre }
            .distinct()
        return joinDescriptionInfo(
            listOf(
                songCountInfo,
                joinDataToInfo(distinctGenres, getTextBasedOnCount(distinctGenres.size, "Genre", "Genres"))
            )
        )
    }
}

class ArtistsDescriptionStrategy: GenericDescriptionStrategy() {
    //displays songs count and distinct albums
    override fun getDescription(optionModel: OptionModel, songs: Collection<SongModel>): String {
        val songCountInfo = super.getDescription(optionModel, songs)
        val distinctAlbums = songs
            .distinctBy { it.albumId }
            .map { it.albumTitle }
        return joinDescriptionInfo(
            listOf(
                songCountInfo,
                joinDataToInfo(distinctAlbums, getTextBasedOnCount(distinctAlbums.size, "Album", "Albums"))
            )
        )
    }
}

class GenresDescriptionStrategy: GenericDescriptionStrategy() {
    //displays songs count and distinct albums
    override fun getDescription(optionModel: OptionModel, songs: Collection<SongModel>): String {
        val songCountInfo = super.getDescription(optionModel, songs)
        val distinctAlbums = songs
            .distinctBy { it.albumId }
            .map { it.albumTitle }
        return joinDescriptionInfo(
            listOf(
                songCountInfo,
                joinDataToInfo(distinctAlbums, getTextBasedOnCount(distinctAlbums.size, "Album", "Albums"))
            )
        )
    }
}