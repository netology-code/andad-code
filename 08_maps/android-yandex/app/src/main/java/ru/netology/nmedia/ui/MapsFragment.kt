package ru.netology.nmedia.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.mapview.MapView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentMapsBinding
import ru.netology.nmedia.ui.extensions.DrawableImageProvider
import ru.netology.nmedia.ui.extensions.ImageInfo

class MapsFragment : Fragment() {
    private val placemarkTapListener = MapObjectTapListener { mapObject, point ->
        Toast.makeText(
            requireContext(),
            (mapObject.userData as String),
            Toast.LENGTH_LONG
        ).show()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentMapsBinding.bind(view)

        val mapView = binding.map
        val mapWindow = mapView.mapWindow
        val yandexMap = mapWindow.map

        subscribeToLifecycle(mapView)

        val target = Point(55.751999, 37.617734)
        addMarker(yandexMap, target)
        moveToMarker(yandexMap, target)

        checkPermissions(mapWindow)
    }

    private fun moveToMarker(
        yandexMap: Map,
        target: Point
    ) {
        val currentPosition = yandexMap.cameraPosition
        yandexMap.move(
            CameraPosition(
                target, 15F, currentPosition.azimuth, currentPosition.tilt,
            ),
            Animation(Animation.Type.SMOOTH, 3F),
            null,
        )
    }

    private fun addMarker(yandexMap: Map, target: Point) {
        val imageProvider =
            DrawableImageProvider(requireContext(), ImageInfo(R.drawable.ic_netology_48dp))

        yandexMap.mapObjects.addPlacemark {
            it.setIcon(imageProvider)
            it.geometry = target
            it.setIcon(imageProvider)
            it.setText("The Moscow Kremlin")
            it.addTapListener(placemarkTapListener)
            it.userData = "Any additional data" // Any
        }
    }

    private fun checkPermissions(mapWindow: MapWindow) {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    enableUserLocation(mapWindow)
                } else {
                    // TODO: show sorry dialog
                }
            }

        when {
            // 1. Проверяем есть ли уже права
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                enableUserLocation(mapWindow)

                MapKitFactory.getInstance()
                    .createLocationManager()
                    .requestSingleUpdate(
                        object : LocationListener {
                            override fun onLocationUpdated(location: Location) {
                                println(location)
                            }

                            override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
                                println(locationStatus)
                            }
                        }
                    )
            }
            // 2. Должны показать обоснование необходимости прав
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // TODO: show rationale dialog
            }
            // 3. Запрашиваем права
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun enableUserLocation(mapWindow: MapWindow) {
        val userLocation = MapKitFactory.getInstance().createUserLocationLayer(mapWindow)
        userLocation.isVisible = true
        userLocation.isHeadingModeActive = true
    }

    private fun subscribeToLifecycle(mapView: MapView) {
        viewLifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(
                    source: LifecycleOwner,
                    event: Lifecycle.Event
                ) {
                    when (event) {
                        Lifecycle.Event.ON_START -> {
                            MapKitFactory.getInstance().onStart()
                            mapView.onStart()
                        }

                        Lifecycle.Event.ON_STOP -> {
                            mapView.onStop()
                            MapKitFactory.getInstance().onStop()
                        }

                        Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)

                        else -> Unit
                    }
                }
            }
        )
    }
}