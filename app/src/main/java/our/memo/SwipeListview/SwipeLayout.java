package our.memo.SwipeListview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import our.memo.R;

public class SwipeLayout extends FrameLayout {

    private ViewDragHelper mDragHelper;

    private int mDragDistance = 0;
    private DragEdge mDragEdge;
    private ShowMode mShowMode;

    private List<SwipeListener> mSwipeListeners = new ArrayList<SwipeListener>();

    public static enum ShowMode {
        LayDown, PullOut
    }

    public static enum DragEdge {
        Left,
        Right,
        Top,
        Bottom
    }

    public enum Status {
        Middle, Open, Close
    }

    public void setDragEdge(DragEdge dragEdge) {
        mDragEdge = dragEdge;
        requestLayout();
    }

    public void setShowMode(ShowMode mode) {
        mShowMode = mode;
        requestLayout();
    }

    public DragEdge getDragEdge() {
        return mDragEdge;
    }

    public ShowMode getShowMode() {
        return mShowMode;
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, mDragHelperCallback);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeLayout);
        int ordinal = a.getInt(R.styleable.SwipeLayout_drag_edge, DragEdge.Right.ordinal());
        mDragEdge = DragEdge.values()[ordinal];
        ordinal = a.getInt(R.styleable.SwipeLayout_show_mode, ShowMode.LayDown.ordinal());
        mShowMode = ShowMode.values()[ordinal];
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mDragEdge == DragEdge.Left || mDragEdge == DragEdge.Right) {
            mDragDistance = getBottomView().getMeasuredWidth();
        } else {
            mDragDistance = getBottomView().getMeasuredHeight();
        }
        Log.i("hello", "drag distance is " + mDragDistance);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return mDragHelper.shouldInterceptTouchEvent(ev);
//    }

        @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if(childCount  != 2){
            throw new IllegalStateException("You need 2  views in SwipeLayout");
        }
        if(!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("The 2 children in SwipeLayout must be an instance of ViewGroup");
        }

        if(mShowMode == ShowMode.PullOut)
            layoutPullOut();
        else if(mShowMode == ShowMode.LayDown)
            layoutLayDown();

        safeBottomView();

//        if(mOnLayoutListeners != null)
//            for(int i = 0; i < mOnLayoutListeners.size(); i++){
//                mOnLayoutListeners.get(i).onLayout(this);
//            }
    }

    void layoutPullOut(){
        Rect rect = computeSurfaceLayoutArea(false);
        getSurfaceView().layout(rect.left, rect.top, rect.right, rect.bottom);
        rect = computeBottomLayoutAreaViaSurface(ShowMode.PullOut, rect);
        getBottomView().layout(rect.left, rect.top, rect.right, rect.bottom);
        bringChildToFront(getSurfaceView());
    }

    void layoutLayDown(){
        Rect rect = computeSurfaceLayoutArea(false);
        getSurfaceView().layout(rect.left, rect.top, rect.right, rect.bottom);
        rect = computeBottomLayoutAreaViaSurface(ShowMode.LayDown, rect);
        getBottomView().layout(rect.left, rect.top, rect.right, rect.bottom);
        bringChildToFront(getSurfaceView());
    }

    private void safeBottomView(){
        Status status = getOpenStatus();
        Log.i("hello", "status: " + status.toString());
        ViewGroup bottom = getBottomView();

        if(status == Status.Close){
            if(bottom.getVisibility() != INVISIBLE)
                bottom.setVisibility(INVISIBLE);
        }else {
            if(bottom.getVisibility() != VISIBLE)
                bottom.setVisibility(VISIBLE);
        }
    }

    public ViewGroup getBottomView() {
        return (ViewGroup) getChildAt(0);
    }

    public ViewGroup getSurfaceView() {
        return (ViewGroup) getChildAt(1);
    }


    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == getSurfaceView() || child == getBottomView();
        }


        //TODO 不是特别的明白
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == getSurfaceView()) {
                switch (mDragEdge) {
                    case Top:
                    case Bottom:
                        return getPaddingLeft();
                    case Left:
                        if (left < getPaddingLeft())
                            return getPaddingLeft();
                        if (left > getPaddingLeft() + mDragDistance)
                            return getPaddingLeft() + mDragDistance;
                        break;
                    case Right:
                        if (left > getPaddingLeft())
                            return getPaddingLeft();
                        if (left < getPaddingLeft() - mDragDistance)
                            return getPaddingLeft() - mDragDistance;
                        break;
                }
            } else if (child == getBottomView()) {
                switch (mDragEdge) {
                    case Top:
                    case Bottom:
                        return getPaddingLeft();
                    case Left:
                        if (mShowMode == ShowMode.PullOut) {
                            if (left > getPaddingLeft())
                                return getPaddingLeft();
                        }
                        break;
                    case Right:
                        if (mShowMode == ShowMode.PullOut) {
                            if (left < getMeasuredWidth() - mDragDistance) {
                                return getMeasuredWidth() - mDragDistance;
                            }
                        }
                        break;
                }
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (child == getSurfaceView()) {
                switch (mDragEdge) {
                    case Left:
                    case Right:
                        return getPaddingTop();
                    case Top:
                        if (top < getPaddingTop())
                            return getPaddingTop();
                        if (top > getPaddingTop() + mDragDistance)
                            return getPaddingTop() + mDragDistance;
                        break;
                    case Bottom:
                        if (top < getPaddingTop() - mDragDistance) {
                            return getPaddingTop() - mDragDistance;
                        }
                        if (top > getPaddingTop()) {
                            return getPaddingTop();
                        }
                }
            } else {
                switch (mDragEdge) {
                    case Left:
                    case Right:
                        return getPaddingTop();
                    case Top:
                        if (mShowMode == ShowMode.PullOut) {
                            if (top > getPaddingTop())
                                return getPaddingTop();
                        } else {
                            if (getSurfaceView().getTop() + dy < getPaddingTop())
                                return getPaddingTop();
                            if (getSurfaceView().getTop() + dy > getPaddingTop() + mDragDistance)
                                return getPaddingTop() + mDragDistance;
                        }
                        break;
                    case Bottom:
                        if (mShowMode == ShowMode.PullOut) {
                            if (top < getMeasuredHeight() - mDragDistance)
                                return getMeasuredHeight() - mDragDistance;
                        } else {
                            if (getSurfaceView().getTop() + dy >= getPaddingTop())
                                return getPaddingTop();
                            if (getSurfaceView().getTop() + dy <= getPaddingTop() - mDragDistance)
                                return getPaddingTop() - mDragDistance;
                        }
                }
            }
            return top;
        }


        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragDistance;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragDistance;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            for (SwipeListener l : mSwipeListeners)
                l.onHandRelease(SwipeLayout.this, xvel, yvel);
            if (releasedChild == getSurfaceView()) {
                processSurfaceRelease(xvel, yvel);
            } else if (releasedChild == getBottomView()) {
                if (getShowMode() == ShowMode.PullOut) {
                    processBottomPullOutRelease(xvel, yvel);
                } else if (getShowMode() == ShowMode.LayDown) {
                    processBottomLayDownMode(xvel, yvel);
                }
            }
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            safeBottomView();
        }
    };

    private GestureDetector gestureDetector = new GestureDetector(getContext(),
            new SwipeDetector());

    class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /**
         * Simulate the touch event lifecycle. If you use SwipeLayout in {@link android.widget
         * .AdapterView}
         * ({@link android.widget.ListView}, {@link android.widget.GridView} etc.) It will
         * manually call
         * {@link android.widget.AdapterView}.performItemClick, performItemLongClick.
         *
         * @param e
         * @return
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            ViewParent t = getParent();
            while (t != null) {
                if (t instanceof AdapterView) {
                    AdapterView view = (AdapterView) t;
                    int p = view.getPositionForView(SwipeLayout.this);
                    if (p != AdapterView.INVALID_POSITION &&
                            view.performItemClick(view.getChildAt(p), p,
                                    view.getAdapter().getItemId(p)))
                        return true;
                } else {
                    if (t instanceof View && ((View) t).performClick())
                        return true;
                }
                t = t.getParent();
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            performLongClick();
        }
    }

    private void processSurfaceRelease(float xvel, float yvel) {
        if (xvel == 0 && getOpenStatus() == Status.Middle) {
            close();
        }
        if (mDragEdge == DragEdge.Left || mDragEdge == DragEdge.Right) {
            if (xvel > 0) {
                if (mDragEdge == DragEdge.Left) {
                    open();
                } else {
                    close();
                }
            } else if (mDragEdge == DragEdge.Left) {
                close();
            } else {
                open();
            }

        } else {
            if (yvel > 0) {
                if (mDragEdge == DragEdge.Top) {
                    open();
                } else {
                    close();
                }
            } else if (mDragEdge == DragEdge.Top) {
                close();
            } else {
                open();
            }
        }
    }

    private void processBottomPullOutRelease(float xvel, float yvel) {

        if (xvel == 0 && getOpenStatus() == Status.Middle)
            close();

        if (mDragEdge == DragEdge.Left || mDragEdge == DragEdge.Right) {
            if (xvel > 0) {
                if (mDragEdge == DragEdge.Left) open();
                else close();
            }
            if (xvel < 0) {
                if (mDragEdge == DragEdge.Left) close();
                else open();
            }
        } else {
            if (yvel > 0) {
                if (mDragEdge == DragEdge.Top) open();
                else close();
            }

            if (yvel < 0) {
                if (mDragEdge == DragEdge.Top) close();
                else open();
            }
        }
    }


    //TODO 测试为空时候的情况  加深题解
    private void processBottomLayDownMode(float xvel, float yvel) {

        if (xvel == 0 && getOpenStatus() == Status.Middle)
            close();

        int l = getPaddingLeft(), t = getPaddingTop();

        if (xvel < 0 && mDragEdge == DragEdge.Right) l -= mDragDistance;
        if (xvel > 0 && mDragEdge == DragEdge.Left) l += mDragDistance;

        if (yvel > 0 && mDragEdge == DragEdge.Top) t += mDragDistance;
        if (yvel < 0 && mDragEdge == DragEdge.Bottom) t -= mDragDistance;
        Log.i("hello", "should process");
        mDragHelper.smoothSlideViewTo(getSurfaceView(), l, t);
        invalidate();
    }

    public void open() {
        open(true, true);
    }

    public void open(boolean smooth) {
        open(smooth, true);
    }

    public void open(boolean smooth, boolean notify) {
        ViewGroup surface = getSurfaceView(), bottom = getBottomView();
        int dx, dy;
        Rect rect = computeSurfaceLayoutArea(true);
        if (smooth) {
            mDragHelper.smoothSlideViewTo(getSurfaceView(), rect.left, rect.top);
        }
        invalidate();
    }

    public void close() {
        close(true, true);
    }

    public void close(boolean smooth) {
        close(smooth, true);
    }

    public void close(boolean smooth, boolean notify) {
        ViewGroup surface = getSurfaceView();
        int dx, dy;
        if (smooth) {
            mDragHelper.smoothSlideViewTo(getSurfaceView(), getPaddingLeft(), getPaddingTop());
        }
        invalidate();
    }

    private Rect computeSurfaceLayoutArea(boolean open) {
        int l = getPaddingLeft(), t = getPaddingTop();
        if (open) {
            if (mDragEdge == DragEdge.Left) l = getPaddingLeft() + mDragDistance;
            else if (mDragEdge == DragEdge.Right) l = getPaddingLeft() - mDragDistance;
            else if (mDragEdge == DragEdge.Top) t = getPaddingTop() + mDragDistance;
            else t = getPaddingTop() - mDragDistance;
        }
        return new Rect(l, t, l + getMeasuredWidth(), t + getMeasuredHeight());
    }

    private Rect computeBottomLayoutAreaViaSurface(ShowMode mode, Rect surfaceArea){
        Rect rect = surfaceArea;

        int bl = rect.left, bt = rect.top, br = rect.right, bb = rect.bottom;
        if(mode == ShowMode.PullOut){
            if (mDragEdge == DragEdge.Left)         bl = rect.left - mDragDistance;
            else if (mDragEdge == DragEdge.Right)   bl = rect.right;
            else if (mDragEdge == DragEdge.Top)     bt = rect.top - mDragDistance;
            else                                    bt = rect.bottom;

            if (mDragEdge == DragEdge.Left || mDragEdge == DragEdge.Right) {
                bb = rect.bottom;
                br = bl + getBottomView().getMeasuredWidth();
            } else {
                bb = bt + getBottomView().getMeasuredHeight();
                br = rect.right;
            }
        }else if(mode == ShowMode.LayDown){
            if(mDragEdge == DragEdge.Left)          br = bl + mDragDistance;
            else if(mDragEdge == DragEdge.Right)    bl = br - mDragDistance;
            else if(mDragEdge == DragEdge.Top)      bb = bt + mDragDistance;
            else                                    bt = bb - mDragDistance;

        }
        return new Rect(bl, bt, br, bb);


    }

    public Status getOpenStatus() {
        int surfaceLeft = getSurfaceView().getLeft();
        int surfaceTop = getSurfaceView().getTop();
        if (surfaceLeft == getPaddingLeft() && surfaceTop == getPaddingTop()) {
            return Status.Close;
        }
        if (surfaceLeft == (getPaddingLeft() - mDragDistance) || surfaceLeft == (getPaddingLeft()
                + mDragDistance) || surfaceTop == (getPaddingTop() - mDragDistance) || surfaceTop
                == (getPaddingTop() + mDragDistance)) {
            return Status.Open;
        }
        return Status.Middle;
    }

    private float sX = -1, sY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();
        ViewParent parent = getParent();

        gestureDetector.onTouchEvent(event);
        Status status = getOpenStatus();
        ViewGroup touching = null;
        if (status == Status.Close) {
            touching = getSurfaceView();
        } else if (status == Status.Open) {
            touching = getBottomView();
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDragHelper.processTouchEvent(event);
                parent.requestDisallowInterceptTouchEvent(true);

                sX = event.getRawX();
                sY = event.getRawY();

                if (touching != null)
                    touching.setPressed(true);

                return true;
            case MotionEvent.ACTION_MOVE: {
                if (sX == -1 || sY == -1) {
                    // Trick:
                    // When in nested mode, we need to send a constructed ACTION_DOWN MotionEvent
                    // to mDragHelper, to help
                    // it initialize itself.
                    event.setAction(MotionEvent.ACTION_DOWN);
                    mDragHelper.processTouchEvent(event);
                    parent.requestDisallowInterceptTouchEvent(true);
                    sX = event.getRawX();
                    sY = event.getRawY();
                    return true;
                }

                float distanceX = event.getRawX() - sX;
                float distanceY = event.getRawY() - sY;
                float angle = Math.abs(distanceY / distanceX);
                angle = (float) Math.toDegrees(Math.atan(angle));

                boolean doNothing = false;
                if (mDragEdge == DragEdge.Right) {
                    boolean suitable = (status == Status.Open && distanceX > 0) || (status ==
                            Status.Close && distanceX < 0);
                    suitable = suitable || (status == Status.Middle);

                    if (angle > 30 || !suitable) {
                        doNothing = true;
                    }
                }

                if (mDragEdge == DragEdge.Left) {
                    boolean suitable = (status == Status.Open && distanceX < 0) || (status ==
                            Status.Close && distanceX > 0);
                    suitable = suitable || status == Status.Middle;

                    if (angle > 30 || !suitable) {
                        doNothing = true;
                    }
                }

                if (mDragEdge == DragEdge.Top) {
                    boolean suitable = (status == Status.Open && distanceY < 0) || (status ==
                            Status.Close && distanceY > 0);
                    suitable = suitable || status == Status.Middle;

                    if (angle < 60 || !suitable) {
                        doNothing = true;
                    }
                }

                if (mDragEdge == DragEdge.Bottom) {
                    boolean suitable = (status == Status.Open && distanceY > 0) || (status ==
                            Status.Close && distanceY < 0);
                    suitable = suitable || status == Status.Middle;

                    if (angle < 60 || !suitable) {
                        doNothing = true;
                    }
                }

                if (doNothing) {
                    parent.requestDisallowInterceptTouchEvent(false);
                    return false;
                } else {
                    if (touching != null) {
                        touching.setPressed(false);
                    }
                    parent.requestDisallowInterceptTouchEvent(true);
                    mDragHelper.processTouchEvent(event);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                sX = -1;
                sY = -1;
                if (touching != null) {
                    touching.setPressed(false);
                }
            }
            default:
                parent.requestDisallowInterceptTouchEvent(true);
                mDragHelper.processTouchEvent(event);
        }

        return true;
    }

    private boolean mTouchConsumedByChild = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Status status = getOpenStatus();
                if(status == Status.Close){
                    mTouchConsumedByChild = childNeedHandleTouchEvent(getSurfaceView(), ev) != null;
                }else if(status == Status.Open){
                    mTouchConsumedByChild = childNeedHandleTouchEvent(getBottomView(), ev) != null;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTouchConsumedByChild = false;
        }
        if(mTouchConsumedByChild) return false;
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    private View childNeedHandleTouchEvent(ViewGroup v, MotionEvent event){
        if(v == null) return null;
        if(v.onTouchEvent(event))
            return v;

        int childCount = v.getChildCount();
        for(int i = childCount - 1; i >= 0; i--){
            View child = v.getChildAt(i);
            if(child instanceof ViewGroup){
                View grandChild = childNeedHandleTouchEvent((ViewGroup) child, event);
                if(grandChild != null)
                    return grandChild;
            }else{
                if(childNeedHandleTouchEvent(v.getChildAt(i), event))
                    return v.getChildAt(i);
            }
        }
        return null;
    }

    private boolean childNeedHandleTouchEvent(View v, MotionEvent event){
        if(v == null)   return false;

        int[] loc = new int[2];
        v.getLocationOnScreen(loc);
        int left = loc[0], top = loc[1];

        if(event.getRawX() > left && event.getRawX() < left + v.getWidth()
                && event.getRawY() > top && event.getRawY() < top + v.getHeight()){
            boolean res =  v.onTouchEvent(event) ;
            return res;
        }

        return false;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public interface SwipeListener {
        public void onStartOpen(SwipeLayout layout);

        public void onOpen(SwipeLayout layout);

        public void onStartClose(SwipeLayout layout);

        public void onClose(SwipeLayout layout);

        public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset);

        public void onHandRelease(SwipeLayout layout, float xvel, float yvel);
    }
}
