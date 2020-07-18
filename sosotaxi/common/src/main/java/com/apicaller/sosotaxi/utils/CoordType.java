package com.apicaller.sosotaxi.utils;

/**
 * @author 骆荟州
 * @createTime
 */

public enum CoordType {
    /** bd09ll坐标 */
    bd09ll("bd09ll"),

    /** bd09mc坐标 */
    bd09mc("bd09mc"),

    /** gcj02ll坐标 */
    gcj02ll("gcj02ll"),

    /** wgs84ll坐标 */
    wgs84ll("wgs84ll");

    private final String string;

    private CoordType(String string) {
        this.string = string;
    }

    public String getStirng() {
        return string;
    }
}