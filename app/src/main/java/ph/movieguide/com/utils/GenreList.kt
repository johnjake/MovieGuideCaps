package ph.movieguide.com.utils

import ph.movieguide.com.data.Genres

class GenreList {
    private val mutableList: MutableList<Genres> = arrayListOf()
    fun genreInstance(): List<Genres> {
        val genAction = Genres(28, "Action")
        val adventure = Genres(12, "Adventure")
        val animation = Genres(16, "Animation")
        val comedy = Genres(35, "Comedy")
        val crime = Genres(80, "Crime")
        val documentary = Genres(99, "Documentary")
        val drama = Genres(18, "Drama")
        val family = Genres(10751, "Family")
        val fantasy = Genres(14, "Fantasy")
        val history = Genres(36, "History")
        val music = Genres(10402, "Music")
        val mystery = Genres(9648, "Mystery")
        val romance = Genres(10749, "Romance")
        val scienceFiction = Genres(878, "Science Fiction")
        val tVMovie = Genres(10770, "TV Movie")
        val thriller = Genres(53, "Thriller")
        val war = Genres(10752, "War")
        val western = Genres(37, "Western")
        mutableList.add(genAction)
        mutableList.add(adventure)
        mutableList.add(animation)
        mutableList.add(comedy)
        mutableList.add(crime)
        mutableList.add(documentary)
        mutableList.add(drama)
        mutableList.add(family)
        mutableList.add(fantasy)
        mutableList.add(history)
        mutableList.add(music)
        mutableList.add(mystery)
        mutableList.add(romance)
        mutableList.add(scienceFiction)
        mutableList.add(tVMovie)
        mutableList.add(thriller)
        mutableList.add(war)
        mutableList.add(western)
        return mutableList
    }



}