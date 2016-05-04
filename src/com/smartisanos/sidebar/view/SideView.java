package com.smartisanos.sidebar.view;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.internal.sidebar.ISidebarService;
import com.smartisanos.sidebar.R;
import com.smartisanos.sidebar.SidebarController;
import com.smartisanos.sidebar.SidebarMode;
import com.smartisanos.sidebar.view.ContentView.ContentType;

public class SideView extends LinearLayout {

    private Button mExit;
    private Button mAdd;
    private ListView mShareList;
    private ResolveInfoListAdapter mAdapter;
    public SideView(Context context) {
        this(context, null);
    }

    public SideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SideView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mExit = (Button) findViewById(R.id.exit);
        updateExitButtonBackground();
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISidebarService sidebarService = ISidebarService.Stub.asInterface(ServiceManager.getService(Context.SIDEBAR_SERVICE));
                if (sidebarService != null) {
                    try {
                        sidebarService.resetWindow();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mAdd = (Button)findViewById(R.id.add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SidebarController sc = SidebarController.getInstance(mContext);
                if(sc.getCurrentContentType() == ContentType.NONE){
                    sc.showContent(ContentType.ADDTOSIDEBAR);
                }else if(sc.getCurrentContentType() == ContentType.ADDTOSIDEBAR){
                    sc.dismissContent();
                }
            }
        });

        mShareList = (ListView) findViewById(R.id.sharelist);
        mAdapter = new ResolveInfoListAdapter(mContext);
        mShareList.setAdapter(mAdapter);
    }

    private void updateExitButtonBackground() {
        if (SidebarController.getInstance(mContext).getSidebarMode() == SidebarMode.MODE_LEFT) {
            mExit.setBackgroundResource(R.drawable.exit_icon_left);
        } else {
            mExit.setBackgroundResource(R.drawable.exit_icon_right);
        }
    }

    public void onSidebarModeChanged(){
        updateExitButtonBackground();
        mAdapter.notifyDataSetChanged();
    }

    public void disable(){
        mExit.setClickable(false);
    }

    public void resume(){
        mExit.setClickable(true);
    }
}
