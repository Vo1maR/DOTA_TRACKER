package com.example.dotan

import android.app.Application
import com.example.dotan.repository.HeroInfo
import com.example.dotan.repository.OpenDotaRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
var heroMap = emptyMap<Int, HeroInfo>()
@HiltAndroidApp
class OpenDotaApplication : Application() {
    @Inject
    lateinit var repository: OpenDotaRepository

    override fun onCreate() {
        super.onCreate()
        // Initialize hero data
        CoroutineScope(Dispatchers.IO).launch {
            val heroData = try {
                repository.getHeroes()
            } catch (e: Exception) {
                emptyList()
            }
            heroMap = heroData.associateBy(
                { it.id },
                { HeroInfo(it.id, localized_name = it.localized_name, primary_attr = it.primary_attr, img = "https://cdn.dota2.com${it.img}") }
            )
        }
    }
}