package com.dragon.smile.data;

import android.content.Context;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2015/5/6 0006.
 */
public class LoginUser extends BmobUser {


    public String phone;
    public RedPacket redPacket;
    private int redPacketSize;
    private long redPacketLastTimeInMills;

    public LoginUser() {
        redPacket = new RedPacket();
    }

    public void save(Context context) {
        redPacketSize = redPacket.getRedPacketSize();
        redPacketLastTimeInMills = redPacket.getLastTimeInMills();
        super.save(context);
    }

    public void update(Context context) {
        redPacketSize = redPacket.getRedPacketSize();
        redPacketLastTimeInMills = redPacket.getLastTimeInMills();
        super.update(context);
    }
}
