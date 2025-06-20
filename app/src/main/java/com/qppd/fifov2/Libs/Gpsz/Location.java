package com.qppd.fifov2.Libs.Gpsz;//package com.buen.rabbithutch.Libs.Gpsz;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//
//
//import androidx.core.app.ActivityCompat;
//
////import com.google.android.gms.location.FusedLocationProviderClient;
////import com.google.android.gms.location.LocationRequest;
////import com.google.android.gms.location.LocationServices;
////import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class Location {
//
//    private Context context;
//    private Activity activity;
//    private FusedLocationProviderClient fusedLocationClient;
//
//    public Location(Context context, Activity activity) {
//        this.context = context;
//        this.activity = activity;
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
//        createLocationRequest();
//
//    }
//
//    public LatLng getLocationFromAddress(Context context, String strAddress) {
//
//        Geocoder coder = new Geocoder(context);
//        List<Address> address;
//        LatLng p1 = null;
//
//        try {
//            // May throw an IOException
//            address = coder.getFromLocationName(strAddress, 5);
//            if (address == null) {
//                return null;
//            }
//
//            Address location = address.get(0);
//            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
//
//        } catch (IOException ex) {
//
//            ex.printStackTrace();
//        }
//
//        return p1;
//    }
//
//    android.location.Location location_;
//
//    public android.location.Location getLastLocation() {
//
//
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return null;
//        }
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(activity, new OnSuccessListener<android.location.Location>() {
//                    @Override
//                    public void onSuccess(android.location.Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            location_ = location;
//                        }
//                    }
//                });
//        return location_;
//    }
//
//    protected void createLocationRequest() {
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    public String getAddress(double latitude, double longitude) throws IOException {
//        Geocoder geocoder;
//        List<Address> addresses;
//
//        geocoder = new Geocoder(context, Locale.getDefault());
//
//        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//
//        //String city = addresses.get(0).getLocality();
//        //String state = addresses.get(0).getAdminArea();
//        //String country = addresses.get(0).getCountryName();
//        //String postalCode = addresses.get(0).getPostalCode();
//        //String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//
//        return addresses.get(0).getAddressLine(0);
//
//    }
//
//}
