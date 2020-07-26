package com.example.gpsmap

import android.annotation.SuppressLint
import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

private const val REQUEST_ACCESS_FINE_LOCATION = 1000
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack

    private val polylineOptions = PolylineOptions().width(10f).color(Color.RED)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)     // 화면이 꺼지지 않게 하기
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT        // 세모 모드로 화면 고정
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager    //프래그먼트 매니저로부터 SupportMapFragment를 얻습니다.
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationInit()
    }

    private fun locationInit() {    //위치정보를 얻는 데 필요한 객체들을 선언.
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        locationCallback = MyLocationCallBack()

        locationRequest = LocationRequest()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY   //가장 정확한 위치를 요청한다.
        // 업데이트 인터벌
        // 위치 정보가 없을 때는 업데이트 안 함
        // 상황에 따라 짧아질 수 있음, 정확하지 않음
        // 다른 앱에서 짧은 인터벌로 위치 정보를 요청하면 짧아질 수 있음
        locationRequest.interval = 10000    //밀리초 단위

        locationRequest.fastestInterval = 5000  // 정확함. 이것보다 짧은 업데이트는 하지 않음
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        permissioncheck(cancel = {
            showPermissionInfoDialog()
        }, ok = {
            mMap.isMyLocationEnabled = true
        })

    }

    override fun onResume(){
        super.onResume()

        permissioncheck(cancel = {
            showPermissionInfoDialog()
        }, ok = {
            addLocationListener()
        })
    }

    @SuppressLint("MissingPermission")
    private fun addLocationListener(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
            locationCallback,
            null)
    }

    private fun showPermissionInfoDialog() {
        alert("현재 위치 정보를 얻으려면 위치 권한이 필요합니다","권한이 필요한 이유") {
            yesButton {
                ActivityCompat.requestPermissions(this@MapsActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_FINE_LOCATION)
            }
            noButton {  }
        }.show()
    }

    override fun onPause() {
        super.onPause()
        removeLocationListener()
    }

    private fun removeLocationListener(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_ACCESS_FINE_LOCATION -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    addLocationListener()
                } else {
                    toast("권한 거부 됨")
                }
                return
            }
        }
    }
    private fun permissioncheck( cancel: () -> Unit,ok: () -> Unit){
        if(ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                cancel()
            } else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_FINE_LOCATION)
            }
        }else{
            ok()
        }
    }
    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val location = locationResult?.lastLocation

            location?.run{
                val latLng = LatLng(latitude,longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17f))

                Log.d("MapActivity","위도: $latitude, 경도: $longitude")
                polylineOptions.add(latLng)

                mMap.addPolyline(polylineOptions)
            }
        }
    }
}