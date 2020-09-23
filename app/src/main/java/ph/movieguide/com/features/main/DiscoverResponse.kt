package ph.movieguide.com.features.main

import ph.movieguide.com.api.MetaData
import ph.movieguide.com.api.Response
import ph.movieguide.com.data.vo.MovieScreen

data class DiscoverResponse(
    val data: DiscoverMetaData = DiscoverMetaData()
) : Response()

data class DiscoverMetaData(
    val results: List<MovieScreen> = emptyList()
) : MetaData()