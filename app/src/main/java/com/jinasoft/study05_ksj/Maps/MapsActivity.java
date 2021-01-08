package com.jinasoft.study05_ksj.Maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jinasoft.study05_ksj.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_PERMISSIONS = 1000;

    private GoogleApiClient mGoogleApiClent;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //구글API 인스턴스 생성
        if (mGoogleApiClent == null) {
            mGoogleApiClent = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this) // 인터페이스를 지정해줘야 에러 풀림 위에 임플먼트 개많음
                    .addApi(LocationServices.API)
                    .build();
        }

        //위치 정보 권한 체크
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        } 온클릭 부분에 Fused 에 의해 자동생성가능
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override // api 접속
    protected void onStart() {
        mGoogleApiClent.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClent.disconnect();
        super.onStop();
    }

    /*
            이 메서드는 한번만 호출됨
            지도를 사용할 준비가 되면 콜백 호출
            여기서 마커나 선을 추가하거나 카메라를 이동
            호주 시드니 근처에 마커를 표시
            구글 플레이 서비스가 설치디어 있어야 한다
            구글 플레이 서비스가 설치되어 있지 않으면 설치 메세지 표시
            * */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //새로운 위치 추가
        LatLng here = new LatLng(37.392439842376646, 126.9539782557827);
        mMap.addMarker(new MarkerOptions().position(here).title("지나소프트"));
        //현지점으로 마커이동
        mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
        //카메라 줌인
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        //마커 클릭 이벤트
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01012341234"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //권한이 승인 된경우
                } else {
                    //권한이 없으면
                    Toast.makeText(this, "권한 체크 거부", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            //다른 권한처리가 있으면 하나 더
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onLastBTN(View view) {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

//            권한이 없으면 요청 다이얼로그
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSIONS);


            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    //현재위치
                    LatLng isMe = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(isMe)
                            .title("현재 위치"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(isMe));
                    //카메라 줌
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
                }
            }
        });
    }
}
