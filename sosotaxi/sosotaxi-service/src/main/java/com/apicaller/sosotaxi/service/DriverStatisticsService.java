package com.apicaller.sosotaxi.service;


import com.apicaller.sosotaxi.entity.DriverStatistics;
import com.apicaller.sosotaxi.feignClients.OrderFeignClient;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/23 6:19 下午
 * @updateTime:
 */
@Component
public class DriverStatisticsService {
    private static Map<Long, DriverStatistics> driverStatisticsMap = new ConcurrentHashMap<>();

    @Resource
    OrderFeignClient orderFeignClient;

    private DriverStatistics getStatistics(long driverId) {
        DriverStatistics statistics = driverStatisticsMap.get(driverId);
        Double rate = orderFeignClient.getDriverAvgRate(driverId);
        if(statistics == null || statistics.getRecordDate() == null || ! isSameDay(statistics.getRecordDate(), new Date())) {
            DriverStatistics newStat = new DriverStatistics();
            newStat.setAccountFlow(0.0);
            newStat.setDriverId(driverId);
            newStat.setOrderNum(0);
            newStat.setRecordDate(new Date());
            newStat.setWorkSeconds(0);
            newStat.setServiceScore(calcServiceScore(rate));
            return statistics;
        }

        statistics.setServiceScore(calcServiceScore(rate));
        return statistics;
    }

    public DriverStatistics setStatistics(DriverStatistics statistics) {
        if(statistics == null) {
            return null;
        }

        Double rate = orderFeignClient.getDriverAvgRate(statistics.getDriverId());
        //测试用

        statistics.setRecordDate(new Date());
        statistics.setServiceScore(calcServiceScore(rate));
        if(rate == null) {
            statistics.setServiceScore(94);
        }
        driverStatisticsMap.put(statistics.getDriverId(), statistics);
        return statistics;
    }

    /**
     * 计算服务分
     */
    private Integer calcServiceScore(Double rate) {
        if(rate == null) {
            return null;
        }
        return 40 + (int)Math.round(rate * 60 / 10);
    }

    /**
     * 判断两个时间是否是同一天
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
