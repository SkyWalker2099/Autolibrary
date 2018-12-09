package com.hsm.zzh.cl.autolibrary.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.hsm.zzh.cl.autolibrary.activity.MachineActivity;
import com.hsm.zzh.cl.autolibrary.info_api.Machine;
import com.hsm.zzh.cl.autolibrary.info_api.MachineOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MapFragment extends Fragment  implements View.OnClickListener {
    private MapView view_mapView = null;
    private ImageView viewSearch;

    private AMap aMap = null;
    private Location mLocation = null;
    private Map<Marker, Integer> markerToId = new HashMap<>();

    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        viewSearch = (ImageView) view.findViewById(R.id.search_image);
        view_mapView = (MapView) view.findViewById(R.id.map_view);

        sp = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);

        view_mapView.setOnClickListener(this);

        view_mapView.onCreate(savedInstanceState);
        if (aMap == null)
            aMap = view_mapView.getMap();

        initMap();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.search_image:{
                // TODO: 18-12-7
                break;
            }
        }
    }

    public void getMachinesByLocation() {
        BmobGeoPoint point = new BmobGeoPoint(mLocation.getLongitude(), mLocation.getLatitude());
        MachineOperation.get_machines_by_location(point, 10.0,
                new FindListener<Machine>() {
            @Override
            public void done(final List<Machine> list, BmobException e) {
                if(e!=null){
                    Log.e("错误", "done: "+ "获取附近机器信息失败");
                    Toast.makeText(getContext(), "获取信息失败", Toast.LENGTH_SHORT)
                            .show();
                    return ;
                }
                Log.i("machine", "done: "+list.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setMarks(list);
                    }
                });
            }
        });
    }

    public void setMarks(List<Machine> machines) {
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
            Marker marker = aMap.addMarker(markeroptions);
            markerToId.put(marker, item.getId());
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
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mLocation = location;
                //在sp中存放经纬度信息
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("location_longitude", ""+location.getLongitude());
                editor.putString("location_latitude",""+location.getLatitude());
                editor.apply();
                getMachinesByLocation();
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // todo 设置机器图片的点击事件
                aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(getActivity(), MachineActivity.class);
                        intent.putExtra("machine_id", markerToId.get(marker));
                        startActivity(intent);
                        return true;
                    }
                });
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
