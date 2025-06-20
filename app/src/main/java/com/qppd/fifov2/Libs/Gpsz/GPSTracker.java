package com.qppd.fifov2.Libs.Gpsz;//package com.buen.rabbithutch.Libs.Gpsz;
//
//import android.Manifest;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.provider.Settings;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.core.app.ActivityCompat;
//
//import java.util.Objects;
//
//public class GPSTracker extends Service implements LocationListener {
//
//    private final Context mContext;
//
//    // flag for GPS status
//    boolean isGPSEnabled = false;
//
//    // flag for network status
//    boolean isNetworkEnabled = false;
//
//    // flag for GPS status
//    boolean canGetLocation = false;
//
//    Location location; // location
//    double latitude; // latitude
//    double longitude; // longitude
//    double speed;
//    // The minimum distance to change Updates in meters
//    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
//
//    // The minimum time between updates in milliseconds
//    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 1 minute
//
//    // Declaring a Location Manager
//    protected LocationManager locationManager;
//
//    public GPSTracker(Context context) {
//        this.mContext = context;
//        getLocation();
//    }
//
//    public Location getLocation() {
//        try {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//            }
//
//            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
//
//            // getting GPS status
//            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            // getting network status
//            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//            if (!isGPSEnabled && !isNetworkEnabled) {
//                // no network provider is enabled
//            } else {
//
//                this.canGetLocation = true;
//                // First get location from Network Provider
//                if (isNetworkEnabled) {
//                    locationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    Log.d("Network", "Network");
//                    if (locationManager != null) {
//                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                            speed = location.getSpeed();
//                        }
//                    }
//                }
//                 //if GPS Enabled get lat/long using GPS Services
//                if (isGPSEnabled) {
//                    if (location == null) {
//                        Objects.requireNonNull(locationManager).requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        Log.d("GPS Enabled", "GPS Enabled");
//                        if (locationManager != null) {
//                            location = locationManager
//                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            if (location != null) {
//                                latitude = location.getLatitude();
//                                longitude = location.getLongitude();
//                                speed = location.getSpeed();
//                            }
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return location;
//    }
//
//    /**
//     * Stop using GPS listener
//     * Calling this function will stop using GPS in your app
//     * */
//    public void stopUsingGPS() {
//        if (locationManager != null) {
//            if (ActivityCompat.checkSelfPermission(GPSTracker.this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//            }
//            locationManager.removeUpdates(GPSTracker.this);
//        }
//    }
//
//    /**
//     * Function to get latitude
//     * */
//    public double getLatitude(){
//        if(location != null){
//            latitude = location.getLatitude();
//        }
//
//        // return latitude
//        return latitude;
//    }
//
//    public double getLongitude(){
//        if(location != null){
//            longitude = location.getLongitude();
//        }
//
//
//        return longitude;
//    }
//    public double getSpeed(){
//        if(location != null){
//
//            speed = location.getSpeed();
//        }
//
//        return speed;
//    }
//
//    /**
//     * Function to check GPS/wifi enabled
//     * @return boolean
//     * */
//    public boolean canGetLocation() {
//        return this.canGetLocation;
//    }
//
//    /**
//     * Function to show settings alert dialog
//     * On pressing Settings button will lauch Settings Options
//     * */
//    public void showSettingsAlert(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("GPS Settings");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
//
//        // On pressing Settings button
//        alertDialog.setPositiveButton("Settings", (dialog, which) -> {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            mContext.startActivity(intent);
//        });
//
//        // on pressing cancel button
//        alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//
//}