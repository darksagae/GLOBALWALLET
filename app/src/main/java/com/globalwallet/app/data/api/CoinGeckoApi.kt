package com.globalwallet.app.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {
    @GET("/api/v3/simple/price")
    suspend fun getSimplePrice(
        @Query("ids") ids: String,
        @Query("vs_currencies") vsCurrencies: String = "usd"
    ): Map<String, Map<String, Double>>

    @GET("/api/v3/search/trending")
    suspend fun getTrending(): TrendingResponse

    @GET("/api/v3/search")
    suspend fun searchNews(@Query("query") query: String): NewsResponse
}

data class TrendingResponse(val coins: List<TrendingCoin>)
data class TrendingCoin(val item: TrendingItem)
data class TrendingItem(val id: String, val name: String, val symbol: String)
data class NewsResponse(val news: List<NewsItem>) 