package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.MinimizedDriver;
import com.apicaller.sosotaxi.entity.UnsettledOrder;

public interface DispatchService {

    /**
     * 按照一定的分配方案进行分配
     */
    void dispatch() throws Exception;

    /**
     * 立即分配最近的司机
     * @param order
     * @return
     */
    MinimizedDriver dispatch(UnsettledOrder order) throws Exception;
}
