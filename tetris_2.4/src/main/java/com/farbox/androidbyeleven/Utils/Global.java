package com.farbox.androidbyeleven.Utils;

import android.content.Context;

import com.farbox.androidbyeleven.Controller.Control.GameState;

/**
 * Created by kys_8 on 16/9/13,0013. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 */
public class Global {
    public static Context applicationContext = null;
    public static LocalizeLog localizeLog = null;

    /**
     * 未设置过值的数如果必要就设置这个值。
     */
    public static final int notSet = -1;

    public static final String tipNotInitOver = "数据 尚未正常初始化";
    public static final String tipNotOperate = "未处理的逻辑";
    public static final String msgReason = "reason";
    public static final String msgResult = "result";


    /**
     * 这个值的设置应该加一个锁，否则有可能出状况
     */
    public static GameState gameState = GameState.ready;

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        Global.gameState = gameState;
    }


}