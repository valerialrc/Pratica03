package mapas.pratica03;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class Mapas extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private final LatLng SEMPEIXE = new LatLng(-20.101503, -42.840673);
    private final LatLng VICOSA = new LatLng(-20.752856, -42.879828);
    private final LatLng DPT = new LatLng(-20.764877, -42.868475);
    private LatLng ATUAL;
    private Marker minhaLoc;
    public LocationManager LOC;
    public Criteria criteria;
    public String provider;
    public int TEMPO = 5000;
    public int DIST = 0;
    public static final int permitir = 1;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapas);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        LOC = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        PackageManager packageManager = getPackageManager();
        boolean GPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

        if (GPS) {
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
        } else {
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        provider = LOC.getBestProvider(criteria, true);

        if (provider == null) {
            Log.e("PROVEDOR", "Nenhum provedor encontrado!");
        } else {
            Log.i("PROVEDOR", "Está sendo utilizado o provedor: " + provider);

            // Pede por permissão se não houver
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION}, permitir);
            }
            LOC.requestLocationUpdates(provider, TEMPO, DIST, this);
        }
    }

    @Override
    protected void onDestroy(){
        LOC.removeUpdates(this);
        Log.w("PROVEDOR", "Provedor " + provider +" parado!");
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location){
        if(location != null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            ATUAL = new LatLng(latitude, longitude);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("LOCATION", "Provedor mudou de estado");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("LOCATION", "Habilitou o provedor");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("LOCATION", "Desabilitou o provedor");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.addMarker(new MarkerOptions()
                .position(SEMPEIXE).title("Casa dos Pais")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        map.addMarker(new MarkerOptions()
                .position(VICOSA).title("Apto em Viçosa")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        map.addMarker(new MarkerOptions()
                .position(DPT).title("DPI")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


        Intent it = getIntent();
        Bundle params = it.getExtras();

        int p = (Integer) params.getInt("position");

        if (p==0)
            onClickSemPeixe(findViewById(R.id.btnSemPeixe));
        else if(p==1)
            onClickVicosa(findViewById(R.id.btnVicosa));
        else
            onClickDpt(findViewById(R.id.btnDpt));
    }

    public void  onClickSemPeixe(View v){
        if(minhaLoc != null)
            minhaLoc.remove();

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(SEMPEIXE, 18);
        map.animateCamera(update);
    }

    public void  onClickVicosa(View v){
        if(minhaLoc != null)
            minhaLoc.remove();

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(VICOSA, 18);
        map.animateCamera(update);
    }

    public void  onClickDpt(View v){
        if(minhaLoc != null)
            minhaLoc.remove();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(DPT, 18);
        map.animateCamera(update);
    }

    public void onClickLoc(View v){
        final Location vicosa = new Location(provider);
        vicosa.setLatitude(VICOSA.latitude);
        vicosa.setLongitude(VICOSA.longitude);

        final Location latual = new Location(provider);
        latual.setLatitude(ATUAL.latitude);
        latual.setLongitude(ATUAL.longitude);

        double dist = latual.distanceTo(vicosa)/1000;

        DecimalFormat df = new DecimalFormat("0.##");

        if(minhaLoc != null)
            minhaLoc.remove();

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        minhaLoc = map.addMarker(new MarkerOptions().position(ATUAL).title("Minha localização atual")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        Toast.makeText(this, "Distância até sua casa: " + df.format(dist) + " km", Toast.LENGTH_LONG).show();


        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ATUAL, 18);
        map.animateCamera(update);
    }
}