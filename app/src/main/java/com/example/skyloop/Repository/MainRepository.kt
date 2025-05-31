package com.example.skyloop.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.skyloop.Domain.FlightModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.example.skyloop.Domain.LocationModel

class MainRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun loadLocation(): LiveData<MutableList<LocationModel>> {
        val listData = MutableLiveData<MutableList<LocationModel>>()
        val ref = firebaseDatabase.getReference("Locations")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<LocationModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(LocationModel::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainRepository", "loadLocation cancelled: ${error.message}")
            }
        })
        return listData
    }

    fun loadFiltered(from: String, to: String): LiveData<MutableList<FlightModel>> {
        val listData = MutableLiveData<MutableList<FlightModel>>()
        val ref = firebaseDatabase.getReference("Flights")
        val query: Query = ref.orderByChild("from").equalTo(from)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
<<<<<<< Updated upstream
                val lists= mutableListOf<FlightModel>()
                for(childSnapshot in snapshot.children){
                    val list=childSnapshot.getValue(FlightModel::class.java)
                    if(list!=null){
                        if(list.To == to){
                            lists.add(list)
=======
                val lists = mutableListOf<FlightModel>()
                for (childSnapshot in snapshot.children) {
                    val flight = childSnapshot.getValue(FlightModel::class.java)
                    if (flight != null) {
                        Log.d("DEBUG", "Read flight: from=${flight.from}, to=${flight.to}")
                        if (flight.to == to) {
                            lists.add(flight)
                            Log.d("DEBUG", "Match found ✅")
>>>>>>> Stashed changes
                        }
                    } else {
                        Log.e("DEBUG", "❌ Failed to map FlightModel from: ${childSnapshot.value}")
                    }
                }
                listData.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DEBUG", "Firebase query cancelled: ${error.message}")
            }
        })

        return listData
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
