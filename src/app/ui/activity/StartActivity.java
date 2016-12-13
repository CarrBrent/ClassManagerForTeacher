
package app.ui.activity;

import myclass.manager.teacher.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.TextView;
import app.ui.FragmentCallback;
import app.ui.activity.setting.LoginActivity;
import app.ui.fragment.ProfileFragment;
import app.ui.fragment.ServiceFragment;
import app.ui.fragment.SettingFragment;
import app.ui.widget.TabView;
import app.ui.widget.TabView.OnTabChangeListener;
import app.util.DialogUtils;
import app.util.FragmentUtils;

public class StartActivity extends FragmentActivity implements OnTabChangeListener, FragmentCallback {

    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private TabView mTabView;
    private TextView mTitleTextView;

    /** 上一次的状态 */
    private int mPreviousTabIndex = 1;
    /** 当前状态 */
    private int mCurrentTabIndex = 1;

    /** 再按一次退出程序*/
    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        mCurrentTabIndex = 1;
        mPreviousTabIndex = 1;
        setupViews();
    }
    private void setupViews()
    {
        setContentView(R.layout.activity_start);
        mTitleTextView = (TextView) findViewById(R.id.text_title);
        mTabView = (TabView) findViewById(R.id.view_tab);
        mTabView.setOnTabChangeListener(this);
        mTabView.setCurrentTab(mCurrentTabIndex);
        mCurrentFragment = new ServiceFragment();
        FragmentUtils.replaceFragment(mFragmentManager, R.id.layout_content,ServiceFragment.class, null, false);
    }
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {/*
            case BaseActivity.REQUEST_OK_LOGIN:
                if (resultCode == RESULT_OK) {
                    ApplicationUtils.showToast(this, R.string.text_loginsuccess);
                    mTitleTextView.setText(R.string.text_tab_profile);
                    final ProfileFragment profileFragment =
                            (ProfileFragment) mFragmentManager.findFragmentByTag(ProfileFragment.class.getSimpleName());
                    if (profileFragment != null) {
                        Log.d(TAG, "ProfileFragment is refreshing.");
                        profileFragment.refreshViews();
                    } else {
                        Log.e(TAG, "ProfileFragment is null.");
                    }
                } else {
                    // 返回原来的页面
                    mTabView.setCurrentTab(mPreviousTabIndex);
                    ApplicationUtils.showToast(this, R.string.toast_login_failed);
                }
                break;

            default:
                break;
        */}
    }


    /* (non-Javadoc)
     * @see app.ui.FragmentCallback#onFragmentCallback(android.support.v4.app.Fragment, int, android.os.Bundle)
     */
    @Override
    public void onFragmentCallback(Fragment fragment, int id, Bundle args) {
        mTabView.setCurrentTab(1);
    }
    /* (non-Javadoc)
     * @see app.ui.widget.TabView.OnTabChangeListener#onTabChange(java.lang.String)
     */
    @Override
    public void onTabChange(String tag) {
    	//查看文件username 如果没有username的id则登录，否则显示个人的信息
    	SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
    	String userId = sharedPreferences.getString("userId", "null");

        if (tag != null) {
           if ("service".equals(tag)) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 1;
                mTitleTextView.setText(R.string.text_tab_service);
                //如果没有登录，则进入登录界面
                if (userId.equals("null")) {
        			startActivity(new Intent(this, LoginActivity.class));
        		}
                replaceFragment(ServiceFragment.class);
            } else if (tag.equals("personal")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 2;
                mTitleTextView.setText(R.string.text_tab_profile);
                //如果没有登录，则进入登录界面
                if (userId.equals("null")) {
        			startActivity(new Intent(this, LoginActivity.class));
        		}
                replaceFragment(ProfileFragment.class);
            } else if (tag.equals("settings")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 3;
                mTitleTextView.setText(R.string.text_tab_setting);
                //如果没有登录，则进入登录界面
                if (userId.equals("null")) {
        			startActivity(new Intent(this, LoginActivity.class));
        		}
                replaceFragment(SettingFragment.class);
            }
        }
    }

    private void replaceFragment(Class<? extends Fragment> newFragment) {
    	
        mCurrentFragment = FragmentUtils.switchFragment(mFragmentManager,
                R.id.layout_content, mCurrentFragment,
                newFragment, null, false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                DialogUtils.showToast(this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
