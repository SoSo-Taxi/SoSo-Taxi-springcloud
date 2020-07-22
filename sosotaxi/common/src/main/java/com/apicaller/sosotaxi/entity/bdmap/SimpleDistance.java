package com.apicaller.sosotaxi.entity.bdmap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/20 12:12 上午
 * @updateTime:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDistance<T> implements Comparable {
    private T pointA;
    private T pointB;

    private double distance;

    private double duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SimpleDistance)) {
            return false;
        }
        //强制类型转换
        SimpleDistance simpleDistance = (SimpleDistance) o;
        //比较属性值
        return Double.compare(distance, simpleDistance.distance) == 0
                && Double.compare(duration, simpleDistance.duration) == 0
                && (pointA != null && simpleDistance.pointA != null && pointA.equals(simpleDistance.pointA))
                && (pointB != null && simpleDistance.pointB != null && pointB.equals(simpleDistance.pointB));
    }

    @Override
    public String toString() {
        return "pointA:" + pointA.toString() + ", pointB:" + pointB.toString() + ", distance:" + distance;
    }


    @Override
    public int compareTo(Object o) {

        if(!(o instanceof SimpleDistance)) {
            return -1;
        }

        SimpleDistance simpleDistance = (SimpleDistance)o;
        return Double.compare(distance, simpleDistance.distance);
    }
}
