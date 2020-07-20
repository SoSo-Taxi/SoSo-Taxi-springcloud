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
        return distance - simpleDistance.distance < 0.000001;
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
        if(distance - simpleDistance.distance <= -0.000001) {
            return -1;
        }
        if(distance - simpleDistance.distance < 0.000001 || distance - simpleDistance.distance > -0.000001) {
            return 0;
        }
        return 1;
    }
}
