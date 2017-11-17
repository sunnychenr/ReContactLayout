package com.example.recontactlayout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.recontactlayout.R;
import com.example.recontactlayout.databeans.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */

public class SideBar extends View {
    // 26个字母
    //即为第一个字符的assic值，此时为★的值(★的assic值为自定义的值为"A".charAt(0) - 1)
    public static final int initASSIC = "A".charAt(0) - 1;
    public static String[] b = {"★", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint mPaint;
    private int mChoose = -1;
    private TouchingLetterChangeListener mListener;
    private RelativeLayout mLetterDialog;
    private List<ContactInfo> mContacts = new ArrayList<>();
    private Context mContext;
    private int mLetterColor;
    private int mLetterSize;
    private int mNoLetterColor;


    public SideBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        mPaint = new Paint();

        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
        mLetterColor = attrsArray.getColor(R.styleable.SideBar_letterColor, 0);
        mLetterSize = attrsArray.getDimensionPixelSize(R.styleable.SideBar_letterSize, 20);
        mNoLetterColor = attrsArray.getColor(R.styleable.SideBar_noLetterColor, 0);
        attrsArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;

        for (int i = 0; i < b.length; i++) {
            if (!isHaveContact(b[i])) {
                mPaint.setColor(mNoLetterColor);
            } else {
                mPaint.setColor(mLetterColor);
            }
            mPaint.setAntiAlias(true);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mPaint.setTextSize(mLetterSize);

            //设置位置
            float xPos = width / 2 - mPaint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, mPaint);
            mPaint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int c = (int) (y / getHeight() * b.length);//当前点击的b数组中的个数
        int oldChoose = mChoose;
        switch (action) {
            case MotionEvent.ACTION_UP:
                //初始化
                mChoose = -1;
                invalidate();
                //隐藏显示
                if (mLetterDialog != null) {
                    mLetterDialog.setVisibility(GONE);
                }
                break;
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (mListener != null) {
                            int assic = initASSIC + c;
                            mListener.onTouchingLetterChange(b[c], assic, y, getHeight());
                        }
                    }
                    //显示
                    if (mLetterDialog != null) {
                        mLetterDialog.setVisibility(VISIBLE);
                    }
                    mChoose = c;
                    invalidate();
                }
        }
        return true;
    }

    public interface TouchingLetterChangeListener {
        /**
         * @param section 字母对应的ASSIC值
         */
        void onTouchingLetterChange(String Letter, int section, float y, float sideBarHeight);
    }

    public void setTouchingLetterChangeListener(TouchingLetterChangeListener listener) {
        mListener = listener;
    }

    public void setLetterDialog(RelativeLayout dialog) {
        this.mLetterDialog = dialog;
    }

    public void setContacts(List<ContactInfo> contacts) {
        this.mContacts = contacts;
    }

    //判断该字母是否有联系人存在
    private boolean isHaveContact(String letter) {
        for (int i = 0; i < mContacts.size(); i++) {
            if (letter.equals(mContacts.get(i).getSortKey())) {
                return true;
            }
        }
        return false;
    }
}
