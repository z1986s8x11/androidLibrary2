package com.zhusx.core.widget.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.zhusx.core.utils._Densitys;


/**
 * Draws a line for each page. The current page line is colored differently than
 * the unselected page lines.
 */
public class Lib_ViewPager_LinePageIndicator extends View implements PageIndicator {
	private static final int INVALID_POINTER = -1;

	private final Paint mPaintUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint mPaintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mListener;
	private int mCurrentPage;
	private boolean mCentered;
	private float mLineWidth;
	private float mGapWidth;

	private int mTouchSlop;
	private float mLastMotionX = -1;
	private int mActivePointerId = INVALID_POINTER;
	private boolean mIsDragging;

	public Lib_ViewPager_LinePageIndicator(Context context) {
		super(context);
		init(context);
	}

	public Lib_ViewPager_LinePageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public Lib_ViewPager_LinePageIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		if (isInEditMode())
			return;

		// Load defaults from resources
		final int defaultSelectedColor = Color.parseColor("#FF33B5E5");
		final int defaultUnselectedColor = Color.parseColor("#FFBBBBBB");
		final float defaultLineWidth = _Densitys.dip2px(context, 12);
		final float defaultGapWidth = _Densitys.dip2px(context, 4);
		final float defaultStrokeWidth = _Densitys.dip2px(context, 1);
		final boolean defaultCentered = true;

		mCentered = defaultCentered;
		mLineWidth = defaultLineWidth;
		mGapWidth = defaultGapWidth;
		_setStrokeWidth(defaultStrokeWidth);
		mPaintUnselected.setColor(defaultUnselectedColor);
		mPaintSelected.setColor(defaultSelectedColor);

		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = ViewConfigurationCompat
				.getScaledPagingTouchSlop(configuration);
	}

	public void _setCentered(boolean centered) {
		mCentered = centered;
		invalidate();
	}

	public boolean _isCentered() {
		return mCentered;
	}

	public void _setUnselectedColor(int unselectedColor) {
		mPaintUnselected.setColor(unselectedColor);
		invalidate();
	}

	public int _getUnselectedColor() {
		return mPaintUnselected.getColor();
	}

	public void _setSelectedColor(int selectedColor) {
		mPaintSelected.setColor(selectedColor);
		invalidate();
	}

	public int _getSelectedColor() {
		return mPaintSelected.getColor();
	}

	public void _setLineWidth(float lineWidth) {
		mLineWidth = lineWidth;
		invalidate();
	}

	public float _getLineWidth() {
		return mLineWidth;
	}

	public void _setStrokeWidth(float lineHeight) {
		mPaintSelected.setStrokeWidth(lineHeight);
		mPaintUnselected.setStrokeWidth(lineHeight);
		invalidate();
	}

	public float _getStrokeWidth() {
		return mPaintSelected.getStrokeWidth();
	}

	public void _setGapWidth(float gapWidth) {
		mGapWidth = gapWidth;
		invalidate();
	}

	public float _getGapWidth() {
		return mGapWidth;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mViewPager == null) {
			return;
		}
		final int count = mViewPager.getAdapter().getCount();
		if (count == 0) {
			return;
		}

		if (mCurrentPage >= count) {
			_setCurrentItem(count - 1);
			return;
		}

		final float lineWidthAndGap = mLineWidth + mGapWidth;
		final float indicatorWidth = (count * lineWidthAndGap) - mGapWidth;
		final float paddingTop = getPaddingTop();
		final float paddingLeft = getPaddingLeft();
		final float paddingRight = getPaddingRight();

		float verticalOffset = paddingTop
				+ ((getHeight() - paddingTop - getPaddingBottom()) / 2.0f);
		float horizontalOffset = paddingLeft;
		if (mCentered) {
			horizontalOffset += ((getWidth() - paddingLeft - paddingRight) / 2.0f)
					- (indicatorWidth / 2.0f);
		}

		// Draw stroked circles
		for (int i = 0; i < count; i++) {
			float dx1 = horizontalOffset + (i * lineWidthAndGap);
			float dx2 = dx1 + mLineWidth;
			canvas.drawLine(dx1, verticalOffset, dx2, verticalOffset,
					(i == mCurrentPage) ? mPaintSelected : mPaintUnselected);
		}
	}

	public boolean onTouchEvent(MotionEvent ev) {
		if (super.onTouchEvent(ev)) {
			return true;
		}
		if ((mViewPager == null) || (mViewPager.getAdapter().getCount() == 0)) {
			return false;
		}

		final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			mLastMotionX = ev.getX();
			break;

		case MotionEvent.ACTION_MOVE: {
			final int activePointerIndex = MotionEventCompat.findPointerIndex(
					ev, mActivePointerId);
			final float x = MotionEventCompat.getX(ev, activePointerIndex);
			final float deltaX = x - mLastMotionX;

			if (!mIsDragging) {
				if (Math.abs(deltaX) > mTouchSlop) {
					mIsDragging = true;
				}
			}

			if (mIsDragging) {
				mLastMotionX = x;
				if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
					mViewPager.fakeDragBy(deltaX);
				}
			}

			break;
		}

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (!mIsDragging) {
				final int count = mViewPager.getAdapter().getCount();
				final int width = getWidth();
				final float halfWidth = width / 2f;
				final float sixthWidth = width / 6f;

				if ((mCurrentPage > 0) && (ev.getX() < halfWidth - sixthWidth)) {
					if (action != MotionEvent.ACTION_CANCEL) {
						mViewPager.setCurrentItem(mCurrentPage - 1);
					}
					return true;
				} else if ((mCurrentPage < count - 1)
						&& (ev.getX() > halfWidth + sixthWidth)) {
					if (action != MotionEvent.ACTION_CANCEL) {
						mViewPager.setCurrentItem(mCurrentPage + 1);
					}
					return true;
				}
			}

			mIsDragging = false;
			mActivePointerId = INVALID_POINTER;
			if (mViewPager.isFakeDragging())
				mViewPager.endFakeDrag();
			break;

		case MotionEventCompat.ACTION_POINTER_DOWN: {
			final int index = MotionEventCompat.getActionIndex(ev);
			mLastMotionX = MotionEventCompat.getX(ev, index);
			mActivePointerId = MotionEventCompat.getPointerId(ev, index);
			break;
		}

		case MotionEventCompat.ACTION_POINTER_UP:
			final int pointerIndex = MotionEventCompat.getActionIndex(ev);
			final int pointerId = MotionEventCompat.getPointerId(ev,
					pointerIndex);
			if (pointerId == mActivePointerId) {
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mActivePointerId = MotionEventCompat.getPointerId(ev,
						newPointerIndex);
			}
			mLastMotionX = MotionEventCompat.getX(ev,
					MotionEventCompat.findPointerIndex(ev, mActivePointerId));
			break;
		}

		return true;
	}

	@Override
	public void _setViewPager(ViewPager viewPager) {
		if (mViewPager == viewPager) {
			return;
		}
		if (mViewPager != null) {
			// Clear us from the old pager.
			mViewPager.setOnPageChangeListener(null);
		}
		if (viewPager.getAdapter() == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}
		mViewPager = viewPager;
		mViewPager.setOnPageChangeListener(this);
		invalidate();
	}

	@Override
	public void _setViewPager(ViewPager view, int initialPosition) {
		_setViewPager(view);
		_setCurrentItem(initialPosition);
	}

	@Override
	public void _setCurrentItem(int item) {
		if (mViewPager == null) {
			throw new IllegalStateException("ViewPager has not been bound.");
		}
		mViewPager.setCurrentItem(item);
		mCurrentPage = item;
		invalidate();
	}

	@Override
	public void _notifyDataSetChanged() {
		invalidate();
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (mListener != null) {
			mListener.onPageScrollStateChanged(state);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (mListener != null) {
			mListener.onPageScrolled(position, positionOffset,
					positionOffsetPixels);
		}
	}

	@Override
	public void onPageSelected(int position) {
		mCurrentPage = position;
		invalidate();

		if (mListener != null) {
			mListener.onPageSelected(position);
		}
	}

	@Override
	public void _setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
		mListener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		float result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
			// We were told how big to be
			result = specSize;
		} else {
			// Calculate the width according the views count
			final int count = mViewPager.getAdapter().getCount();
			result = getPaddingLeft() + getPaddingRight()
					+ (count * mLineWidth) + ((count - 1) * mGapWidth);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return (int) Math.ceil(result);
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(int measureSpec) {
		float result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the height
			result = mPaintSelected.getStrokeWidth() + getPaddingTop()
					+ getPaddingBottom();
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return (int) Math.ceil(result);
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		mCurrentPage = savedState.currentPage;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPage = mCurrentPage;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPage;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPage = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPage);
		}

		@SuppressWarnings("UnusedDeclaration")
		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}