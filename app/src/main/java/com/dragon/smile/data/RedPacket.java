package com.dragon.smile.data;

import android.os.SystemClock;

import java.util.Random;

/**
 * Created by Administrator on 2015/5/31 0031.
 */
public class RedPacket {
    private final static long GET_RED_PACKET_TIME_DURING = 24 * 60 * 60 * 1000;
    private long lastTimeMills;
    private int size;

    public boolean canGetRedPacket(long currentTime) {
        if (size <= 0 && (currentTime - lastTimeMills > GET_RED_PACKET_TIME_DURING)) {
            return true;
        } else
            return false;
    }

    public int generateRedPacket(long currentTime) {
        if (!canGetRedPacket(currentTime)) {
            return -1;
        }

        lastTimeMills = currentTime;
        Random random = new Random(SystemClock.elapsedRealtime());
        size = random.nextInt(5) + 1;
        return size;
    }

    public int getRedPacketSize() {
        return size;
    }

    public long getLastTimeInMills() {
        return lastTimeMills;
    }
}
