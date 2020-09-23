package ph.movieguide.android_baseplate_persistence.mapper

interface LocalDBMapper<in DBUser, T> {
    fun mapFromDB(from: DBUser): T
}