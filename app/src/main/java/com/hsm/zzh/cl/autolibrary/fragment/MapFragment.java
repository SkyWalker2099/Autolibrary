package com.hsm.zzh.cl.autolibrary.fragment;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.info_api.Machine;

import cn.bmob.v3.datatype.BmobGeoPoint;

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

    // TODO 获取附近机器信息列表
    public Machine[] getMachines() {
        Machine[] machines = new Machine[2];
        BmobGeoPoint point1 = new BmobGeoPoint(
                mLocation.getLongitude() - 0.1, mLocation.getLatitude() - 0.1);
        BmobGeoPoint point2 = new BmobGeoPoint(
                mLocation.getLongitude() + 0.1, mLocation.getLatitude() + 0.1);
        machines[0] = new Machine(point1, 1, "测试点1");
        machines[1] = new Machine(point2, 2, "测试点2");
        return machines;
    }

    public void setMarks() {
        Machine[] machines = getMachines();
        for (Machine item : machines) {
            MarkerOptions markeroptions = new MarkerOptions();
            LatLng latLng = new LatLng(item.getLocation().getLatitude(), item.getLocation().getLongitude());
            markeroptions.position(latLng);

            markeroptions.title(item.getId() + "");
            markeroptions.snippet(item.getShortdesc());
            markeroptions.draggable(false);
            markeroptions.visible(true);
            markeroptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources()
                    , R.drawable.locate)));
            aMap.addMarker(markeroptions);
        }
    }

    private void initMap() {
        final MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationType(MyLocationStyle.);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(1000);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.radiusFillColor(0xffffff);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_locate));
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mLocation = location;
                setMarks();
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // todo 设置机器图片的点击事件
                Log.i("地图标志点击", "onMarkerClick: " + marker.getTitle());
                return true;
            }
        });

        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomPosition(0);
        uiSettings.setMyLocationButtonEnabled(false);
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
