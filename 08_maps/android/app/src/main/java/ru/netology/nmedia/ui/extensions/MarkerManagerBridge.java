package ru.netology.nmedia.ui.extensions;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.collections.MarkerManager;

import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import ru.netology.nmedia.R;

/**
 * Данный класс нужен, чтобы исправить баг IDE,
 * при котором становится невозможным работать с классом MarkerManager.Collection из Kotlin кода
 */
public class MarkerManagerBridge {

    private final Context context;

    public MarkerManagerBridge(final Context context) {
        this.context = context;
    }

    public void addMarker(
            final GoogleMap googleMap,
            final LatLng target,
            final Function1<Marker, Unit> clickListener
    ) {
        final MarkerManager markerManager = new MarkerManager(googleMap);
        final MarkerManager.Collection collection = markerManager.newCollection();

        final MarkerOptions options = new MarkerOptions();
        options.position(target);
        MarkerOptionsKtKt.icon(
                options,
                Objects.requireNonNull(ContextCompat.getDrawable(context, R.drawable.ic_netology_48dp))
        );
        options.title("The Moscow Kremlin");
        collection.addMarker(options);
        collection.setOnMarkerClickListener(marker -> {
            clickListener.invoke(marker);
            return true;
        });
    }
}
