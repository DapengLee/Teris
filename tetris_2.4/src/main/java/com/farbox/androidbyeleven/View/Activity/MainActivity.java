package com.farbox.androidbyeleven.View.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.farbox.androidbyeleven.Controller.Control.GameThread;
import com.farbox.androidbyeleven.Controller.Control.MHandler;
import com.farbox.androidbyeleven.Controller.M2V.impl.BeakerNotify;
import com.farbox.androidbyeleven.Controller.V2M.impl.BeakerService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisMoveGetterService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisMoveInteractiveService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisMoveSetterService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisShowGetterService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisShowInteractiveService;
import com.farbox.androidbyeleven.Model.Impl.BeakerModel;
import com.farbox.androidbyeleven.Model.Impl.TetrisMoveModel;
import com.farbox.androidbyeleven.Model.Impl.TetrisShowModel;
import com.farbox.androidbyeleven.R;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;
import com.farbox.androidbyeleven.View.Weight.Beaker;
import com.farbox.androidbyeleven.View.Weight.MyTableRow;
import com.farbox.androidbyeleven.View.Weight.TetrisMove;
import com.farbox.androidbyeleven.View.Weight.TetrisShow;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private Beaker beaker = null;
    private LinearLayout llNextSquare = null;
    private FrameLayout frameLayout = null;
    private Switch mSwitch = null;
    private MyTableRow myTableRow = null;
    private final IntentFilter intentFilter = new IntentFilter();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_OFF://关闭屏幕
                case Intent.ACTION_CLOSE_SYSTEM_DIALOGS://长按电源键出现关机选项
                    play2Pause();
                    break;
                case Intent.ACTION_SCREEN_ON:
                    LogUtil.i(LogUtil.msg() + "ACTION_SCREEN_ON");
                    break;
                case Intent.ACTION_USER_PRESENT://
                    LogUtil.i(LogUtil.msg() + "ACTION_USER_PRESENT");
                    break;
            }
        }
    };

    @Override
    protected void beforeView() {
        super.beforeView();
        // 屏幕灭屏广播
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        this.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 设置即将加载的布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        this.mSwitch = (Switch) findViewById(R.id.switchBtn);
        this.mSwitch.setOnCheckedChangeListener(this);
        this.llNextSquare = (LinearLayout) findViewById(R.id.ll_nextSquare);
        this.frameLayout = (FrameLayout) findViewById(R.id.fl_viewGroup);
        this.myTableRow = (MyTableRow) findViewById(R.id.mTableRow);
//        findViewById(R.id.tv_score).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Global.applicationContext, "测试专用", Toast.LENGTH_LONG).show();
//                iUserIntent.ready2Play();
//            }
//        });
    }

    @Override
    protected void initData() {
        //①连接Beaker、Controller、Model三者。
        this.beaker = (Beaker) findViewById(R.id.tetrisBeaker);
        BeakerNotify beakerNotify = new BeakerNotify(this.beaker);
        BeakerModel beakerModel = BeakerModel.getInstance(beakerNotify);//这里必须用带参数的获取
        BeakerService beakerService = new BeakerService(beakerModel, beakerModel);
        this.beaker.setService(beakerService);//设置服务员
        this.beaker.setServerCondition(beakerService);//设置服务员
        //②连接showTetris、Controller、Model三者。
        TetrisShowModel tetrisShowModel = new TetrisShowModel();
        TetrisShowGetterService serverSquare = new TetrisShowGetterService(tetrisShowModel);
        TetrisShowInteractiveService serverInteractive = new TetrisShowInteractiveService(tetrisShowModel);
        this.llNextSquare.addView(TetrisShow.getInstance(serverSquare, serverInteractive));
        //③连接moveTetris、Controller、Model三者
        TetrisMoveModel tetrisMoveModel = new TetrisMoveModel();
        TetrisMoveGetterService tetrisMoveGetterService = new TetrisMoveGetterService(tetrisMoveModel);
        TetrisMoveSetterService tetrisMoveSetterService = new TetrisMoveSetterService(tetrisMoveModel);
        TetrisMoveInteractiveService tetrisMoveInteractiveService = new TetrisMoveInteractiveService(tetrisMoveModel);
        TetrisMove tetrisMove = TetrisMove.getInstance(tetrisMoveGetterService, tetrisMoveSetterService, tetrisMoveInteractiveService);
        this.frameLayout.addView(tetrisMove);
        //④连接Gesture、Controller、Model三者
        this.myTableRow.setParam(tetrisMoveGetterService, tetrisMoveInteractiveService, this.beaker);
        //先初始化一次Thread,免得以后调用麻烦。
        GameThread.getInstance(new MHandler(this.beaker, tetrisMoveInteractiveService), tetrisMoveInteractiveService);
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (Global.getGameState()) {
            case ready:
                GameThread.getInstance().ready2Play();
                break;
            case noticeGameWait:
                GameThread.getInstance().pause2Play();
                break;
            case moving:
                GameThread.getInstance().paly2Pause();
                break;
            default:
                LogUtil.e(LogUtil.msg() + Global.tipNotOperate);
                break;
        }
    }

    private void play2Pause() {
        if (this.mSwitch.isChecked()) {
            this.mSwitch.setChecked(false);
        }


    }
}
