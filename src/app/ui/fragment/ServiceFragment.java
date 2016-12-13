package app.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import myclass.manager.teacher.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import app.ui.BaseFragment;
import app.ui.activity.barcode.SignInActivity;
import app.ui.activity.myclass.MyClassActivity;
import app.ui.adapter.ViewPaperAdapter;

public class ServiceFragment extends BaseFragment implements Runnable{

	private ViewPager mViewPager;
	private ViewGroup group;
	private ViewPaperAdapter mAdapter;
	private List<View> mViewPicture;
	private ImageView[] mImageViews = null;
	private ImageView imageView = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	private final OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_1:
				startActivity(new Intent(getActivity(), SignInActivity.class));
				break;
			case R.id.layout_3:
				startActivity(new Intent(getActivity(), MyClassActivity.class));
				break;
			default:
				break;
			}
			
		}
	};
	
	private final Handler viewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mViewPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_service, container, false);
	}
	/* (non-Javadoc)
	 * @see app.ui.BaseFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		
		//将设置好的onClickListener关联到相应的layout
		view.findViewById(R.id.layout_1).setOnClickListener(onClickListener);
		view.findViewById(R.id.layout_2).setOnClickListener(onClickListener);
		view.findViewById(R.id.layout_3).setOnClickListener(onClickListener);
		view.findViewById(R.id.layout_4).setOnClickListener(onClickListener);
		//guidepage
		mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
		group = (ViewGroup)view.findViewById(R.id.viewGroup);

		mViewPicture = new ArrayList<View>();
		ImageView img1 = new ImageView(getActivity());
		img1.setBackgroundResource(R.drawable.advertising_default_1);
		mViewPicture.add(img1);

		ImageView img2 = new ImageView(getActivity());
		img2.setBackgroundResource(R.drawable.advertising_default_2);
		mViewPicture.add(img2);

		ImageView img3 = new ImageView(getActivity());
		img3.setBackgroundResource(R.drawable.advertising_default_3);
		mViewPicture.add(img3);

		ImageView img4 = new ImageView(getActivity());
		img4.setBackgroundResource(R.drawable.advertising_default);
		mViewPicture.add(img4);

		mImageViews = new ImageView[mViewPicture.size()];

		for (int i = 0; i < mViewPicture.size(); i++) {
			imageView = new ImageView(getActivity());
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(5, 5, 5, 5);
			mImageViews[i] = imageView;
			if (i == 0) {
				mImageViews[i].setBackgroundResource(R.drawable.ic_viewpager_select);
			} else {
				mImageViews[i].setBackgroundResource(R.drawable.ic_viewpager_noselect);
			}
			group.addView(mImageViews[i]);
		}

		mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
		mAdapter = new ViewPaperAdapter(mViewPicture);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnTouchListener(new GuideOnTouchListener());
		new Thread(this).start();
	}


	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			what.getAndSet(arg0);
			for (int i = 0; i < mImageViews.length; i++) {
				mImageViews[arg0].setBackgroundResource(R.drawable.ic_viewpager_select);
				if (arg0 != i) {
					mImageViews[i].setBackgroundResource(R.drawable.ic_viewpager_noselect);
				}
			}
		}
	}

	private final class GuideOnTouchListener implements OnTouchListener{

		/* (non-Javadoc)
		 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
		 */
		@Override
		public boolean onTouch(View v, MotionEvent event) {
//			switch (v.getId()) {
//			case R.id.layout_1:
//				startActivity(new Intent(getActivity(), AboutActivity.class));
//				break;
//
//			default:
//				break;
//			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				isContinue = false;
				break;
			case MotionEvent.ACTION_UP:
				isContinue = true;
				break;
			default:
				isContinue = true;
				break;
			}
			return false;
		}

	}

	private void whatOption() {
		what.incrementAndGet();
		if (what.get() > mImageViews.length - 1) {
			what.getAndAdd(-4);
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (true) {
			if (isContinue) {
				viewHandler.sendEmptyMessage(what.get());
				whatOption();
			}
		}
	}
}
