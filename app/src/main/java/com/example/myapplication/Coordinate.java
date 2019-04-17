package com.example.myapplication;

public class Coordinate {
    public int dbm, x, y, z;

    Coordinate(int dbm, int x, int y) {
        this.dbm = - Math.abs(dbm);
        this.x = x;
        this.y = y;
        this.z = 1;
    }
}
