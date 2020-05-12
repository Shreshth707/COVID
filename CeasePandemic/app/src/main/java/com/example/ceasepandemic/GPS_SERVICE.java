package com.example.ceasepandemic;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import static android.content.ContentValues.TAG;

public class GPS_SERVICE extends Service {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private DatabaseReference refDb = FirebaseDatabase.getInstance().getReference();

    private String userId = FirebaseAuth.getInstance().getUid();
    private UserObject currUser = new UserObject(userId);
    private ArrayList<UserObject> contactList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }



    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SSB Log", "onStartCommand");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For foreground service
            Intent notificationIntent = new Intent(this, GPS_SERVICE.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            // Creating channel for notification
            String id = GPS_SERVICE.class.getSimpleName();
            String name = GPS_SERVICE.class.getSimpleName();
            NotificationChannel notificationChannel = new NotificationChannel(id,
                    name, NotificationManager.IMPORTANCE_NONE);
            NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            service.createNotificationChannel(notificationChannel);

            // Foreground notification
            Notification notification = new Notification.Builder(this, id)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText("Show service running reason to user")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setTicker("Ticker text")
                    .build();

            startForeground(9, notification);
        }
        // Service logic here
        requestLocationUpdates();

        return Service.START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public void requestLocationUpdates(){
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationRequest  = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);
        locationRequest.setInterval(8000);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //add Location to Database
                currUser.setGeoFire(new GeoLocation(locationResult.getLastLocation().getLatitude()
                        ,locationResult.getLastLocation().getLongitude()));
                GeoFire geoFire = new GeoFire(refDb.child("location"));
                geoFire.setLocation(userId, new GeoLocation(currUser.getGeoLocation().latitude, currUser.getGeoLocation().longitude)
                        , new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                Log.e("GeoFire","Database Location Updated");
                            }
                        });
                findNearbyPeople();
                for (UserObject contact : contactList){
                    getNearbyPeopleStatus(contact);
                }
                for (UserObject contact : contactList){
                    boolean status = checkNearbyPeopleStatus(contact);
                    if (status){
                        currUser.setStatus(contactList.get(0).getStatus());
                        if (currUser.getStatus() != null){
                            if (currUser.getStatus().equals("-1")){
                                currUser.setStatus("0");
                                Log.e("Status","Current User Status Updated");
                                //update status in database
                                updateFirebaseDatabase();
                                break;
                            }
                        }
                    }
                }
            }
        },getMainLooper());
    }

    private void updateFirebaseDatabase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user").child(currUser.getUid());
        Map mUser = new HashMap();
        mUser.put("status",currUser.getStatus());
        ref.updateChildren(mUser);

        Log.e("status","FirebaseDatabase Status Changed");
    }

    private boolean checkNearbyPeopleStatus(UserObject contact) {
        Log.e("status","Checking status " + contact.getStatus());
      if (contact.getStatus()!=null){
          if (contact.getStatus().equals("0")){
              Log.e("Return status","True");
              return true;
          }else if (contact.getStatus().equals("1")){
              Log.e("Return status","True");
              return true;
          }else {
              Log.e("Return status","False");
              return false;
          }
      }else {
          Log.e("Return status","False");
          return false;
      }

    }

    private void getNearbyPeopleStatus(final UserObject contact) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = ref.child(contact.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("status").getValue()!=null){
                        contact.setStatus(dataSnapshot.child("status").getValue().toString());
                        Log.e("Contact List","ContactList Updated with status");
                        Log.e("Contact List Size","The size Of the Contact List is " + contactList.size());
                        Log.e("contact's status",contact.getStatus());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void findNearbyPeople() {
        GeoFire geoFire = new GeoFire(refDb.child("location"));
        GeoQuery geoQuery = geoFire.queryAtLocation(currUser.getGeoLocation(),6);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                boolean exits = false;
                Log.e("GeoFire Query Key", key);
                for (UserObject contact:contactList){
                    if (key == contact.getUid()){
                        exits = true;
                    }
                }
                if(!exits){
                    UserObject contact = new UserObject(key);
                    contactList.add(contact);
                    Log.e("ContactList","Contact Added to Contact List");
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

}
