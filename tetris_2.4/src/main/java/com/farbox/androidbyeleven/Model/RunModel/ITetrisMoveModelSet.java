package com.farbox.androidbyeleven.Model.RunModel;

import android.graphics.Point;

/**
 * Created by Tom on 2016/11/8.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 */
public interface ITetrisMoveModelSet {
    /**
     * 设置当前正在显示的矩阵
     *
     * @return
     */
    void setCurrentMatrix(int[][] currentMatrix);

    /**
     * 设置在背景矩阵中的逻辑位置
     */
    void setLogicPos(Point pos);
}
