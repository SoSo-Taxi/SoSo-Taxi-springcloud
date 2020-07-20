package com.apicaller.sosotaxi.entity.bdmap;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/20 12:08 上午
 * @updateTime:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimplePosition<T> {
    private T tag;

    private GeoPoint point;
}
