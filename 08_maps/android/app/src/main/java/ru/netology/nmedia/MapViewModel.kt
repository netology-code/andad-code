package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import ru.netology.nmedia.dto.Place

class MapViewModel : ViewModel() {
    private val _places = MutableLiveData(
        List(1000) {
            Place(
                "Moscow $it", LatLng(
                    54.9238448, 83.0808806
                )
            )
        }
    )
    val places: LiveData<List<Place>>
        get() = _places
    private val _selectedPlace = MutableLiveData<Place>()
    val selectedPlace: LiveData<Place>
        get() = _selectedPlace

    fun selectPlace(place: Place) {
        _selectedPlace.value = place
    }
}
