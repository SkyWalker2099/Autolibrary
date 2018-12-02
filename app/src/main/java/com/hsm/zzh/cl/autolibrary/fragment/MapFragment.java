package com.hsm.zzh.cl.autolibrary.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.hsm.zzh.cl.autolibrary.R;

public class MapFragment extends Fragment {
    private MapView view_mapView = null;

    private AMap aMap = null;
    private Location mLocation = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        view_mapView = (MapView) view.findViewById(R.id.map_view);
        view_mapView.onCreate(savedInstanceState);
        if (aMap == null)
            aMap = view_mapView.getMap();

        initMap();
        return view;
    }

    private void initMap() {
        final MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationType(MyLocationStyle.);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(1000);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.radiusFillColor(0xffffff);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mLocation = location;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view_mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        view_mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        view_mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        view_mapView.onSaveInstanceState(outState);
    }
}
