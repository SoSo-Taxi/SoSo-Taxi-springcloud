package com.apicaller.sosotaxi.controller;


import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.entity.dispatch.dto.GenerateOrderDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.apicaller.sosotaxi.entity.dispatch.response.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
@RestController
@RequestMapping("/dispatch")
public class DispatchController {

    /**
     * 获取给定地点附近的司机。
     * @param lat
     * @param lng
     * @return 附近司机列表
     */
    @GetMapping("/getNearbyDrivers")
    public ResponseBean getNearbyDrivers(double lat, double lng) {
        List<GeoPoint> result = new ArrayList<GeoPoint>();
        result.add(new GeoPoint(40.058922, 116.312615));
        result.add(new GeoPoint(40.058939, 116.312675));
        result.add(new GeoPoint(40.058522, 116.382615));
        return new ResponseBean(200, null, result);
    }

    /**
     * 获取给定地点可用的服务。
     */
    @GetMapping("/getServices")
    public ResponseBean getAvailableService(double lat, double lng, Date date) {
        List<AvailableServiceTypeResponse> result = new ArrayList<AvailableServiceTypeResponse>();
        result.add(new AvailableServiceTypeResponse(0, 4));
        result.add(new AvailableServiceTypeResponse(1, 4));
        return new ResponseBean(200, null, result);
    }

    /**
     * 获取计价规则。
     */
    @GetMapping("/getPrice")
    public ResponseBean getPricingMethod(double lat, double lng, Date date, Short serviceType) {
        List<AvailableServiceCalResponse> result = new ArrayList<AvailableServiceCalResponse>();
        result.add(new AvailableServiceCalResponse(0, 1.2, 0.2, "CNY", 0.2, 6.5));
        return new ResponseBean(200, null, result);
    }

    /**
     * 获取预计接驾时间。
     */
    @GetMapping("/getEstimateTime")
    public ResponseBean getEstimateTime(double lat, double lng, Short serviceType) {
        List<EstimateTimeResponse> result = new ArrayList<EstimateTimeResponse>();
        result.add(new EstimateTimeResponse(0, 102));
        result.add(new EstimateTimeResponse(1, 66));
        return new ResponseBean(200, null, result);
    }

    /**
     * 获取预计价格。
     */
    @GetMapping("/getEstimatePrice")
    public ResponseBean getEstimatePrice(double departLat, double departLng,
                                                        double destLat, double destLng, Date date, Short serviceType) {

        List<EstimatePriceResponse> result = new ArrayList<EstimatePriceResponse>();
        result.add(new EstimatePriceResponse(0, 3.3, 9.6, 480, "CNY"));
        result.add(new EstimatePriceResponse(1, 4.4, 12.4, 600, "CNY"));
        return new ResponseBean(200, null, result);
    }

    /**
     * 生成打车订单。
     */
    @PostMapping("/generateOrder")
    public ResponseBean generateOrder(@RequestBody GenerateOrderDTO order) {
        return new ResponseBean(200, null, 1234);
    }

    /**
     * 获取订单状态。
     */
    @GetMapping("/getOrderStatus")
    public ResponseBean getOrderStatus(long orderId) {
        return new ResponseBean(200, null, "pending");
    }

    /**
     * 获取司机和车辆信息。
     * 仅在司机接单到到达终点这段时间内可以查询。
     */
    @GetMapping("/getDriverCarInfo")
    public ResponseBean getDriverCarInfo(long orderId) {
        DriverCarInfoResponse result = new DriverCarInfoResponse();
        result.setServiceType((short) 0);
        result.setCarBrand("大众");
        result.setCarModel("迈腾");
        result.setCarColor("黑色");
        result.setDriverId(1);
        result.setDriverName("骆荟州");
        result.setPhoneNumber("13996996996");
        result.setLicensePlate("XXXXXXX");
        return new ResponseBean(200, null, result);
    }

    /**
     * 获取司机当前位置。
     */
    @GetMapping("/getDriverPosition")
    public ResponseBean getDriverPosition(long orderId) {
        return new ResponseBean(200, null, new GeoPoint(100, 100));
    }


}
