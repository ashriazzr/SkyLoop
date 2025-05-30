package com.example.skyloop.ViewModel

import androidx.lifecycle.LiveData
import com.example.skyloop.Domain.LocationModel
import com.example.skyloop.Repository.MainRepository

class MainViewModel {
    private val repository=MainRepository()
        fun loadLocations():LiveData<MutableList<LocationModel>>{
            return repository.loadLocation()
    }
}