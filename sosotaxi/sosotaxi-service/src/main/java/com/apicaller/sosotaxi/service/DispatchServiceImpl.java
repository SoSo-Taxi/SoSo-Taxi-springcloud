package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.bdmap.AroundSearchDriverResponse;
import com.apicaller.sosotaxi.entity.bdmap.SimpleDistance;
import com.apicaller.sosotaxi.entity.bdmap.SimplePosition;
import com.apicaller.sosotaxi.utils.BDmapUtil;
import com.apicaller.sosotaxi.utils.CoordType;
import com.apicaller.sosotaxi.utils.YingYanUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/22 9:33 上午
 * @updateTime:
 */
@Service
public class DispatchServiceImpl {

    public List<GeoPoint> getNearbyDrivers(double lat, double lng) {
        List<AroundSearchDriverResponse> res = YingYanUtil
                .aroundSearchDriver(lat, lng, CoordType.bd09ll, 5000, false, null);

        if(res == null || res.isEmpty()) {
            return null;
        }
        List<GeoPoint> result = new ArrayList<GeoPoint>();
        for (AroundSearchDriverResponse driver : res) {
            result.add(driver.getPoint());
        }
        return result;
    }

    /**
     * 获取预计上车时间。这里只是简单找到最近的车辆，然后计算车辆到所给位置的驾车时间。
     */
    public int getEstimateTime(double lat, double lng, Short serviceType) {
        List<AroundSearchDriverResponse> res = YingYanUtil
                .aroundSearchDriver(lat, lng, CoordType.bd09ll, 5000, false, serviceType);

        if(res == null || res.isEmpty()) {
            return 1800;
        }

        List<SimplePosition<String>> posList = res.stream()
                .map(driver -> {
                    return new SimplePosition<String>("a", driver.getPoint());
                })
                .collect(Collectors.toList());

        List<SimpleDistance<String>> distanceList = BDmapUtil
                .batchCalcDistance(new SimplePosition<>("b", new GeoPoint(lat, lng)), posList, CoordType.bd09ll);
        if(distanceList == null || distanceList.isEmpty()) {
            return 1800;
        }
        Collections.sort(distanceList);
        return (int)distanceList.get(0).getDuration();

    }


}
