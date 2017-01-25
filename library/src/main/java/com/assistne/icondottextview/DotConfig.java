package com.assistne.icondottextview;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by assistne on 17/1/20.
 */
// TODO: 17/1/20 文本长度
// TODO: 17/1/20 区分有文字和无文字
// TODO: 17/1/24 文本不能超出dot范围
public class DotConfig implements Config {
    private static final int DEFAULT_SIZE = 35;
    @ColorInt private static final int DEFAULT_COLOR = Color.RED;
    private static final int DEFAULT_TEXT_SIZE = 16;
    @ColorInt private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    int size = DEFAULT_SIZE;
    @ColorInt int color = DEFAULT_COLOR;

    @Nullable
    TextConfig textConfig;
    private Paint mPaint;
    private int mMaxWidth = Integer.MAX_VALUE;
    private int mMaxHeight = Integer.MAX_VALUE;

    public DotConfig(TypedArray typedArray) {
        if (typedArray != null) {
            size = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_size, DEFAULT_SIZE);
            color = typedArray.getColor(R.styleable.IconDotTextView_dot_color, DEFAULT_COLOR);
            String text = typedArray.getString(R.styleable.IconDotTextView_dot_text);
            int textSize = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_textSize, DEFAULT_TEXT_SIZE);
            int textColor = typedArray.getColor(R.styleable.IconDotTextView_dot_textColor, DEFAULT_TEXT_COLOR);
            textConfig = new TextConfig(text, textSize, textColor);
            textConfig.setMaxHeight(size);
            textConfig.setMaxWidth(size);
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public int getHeight() {
        return Math.min(getDesiredHeight(), mMaxHeight);
    }

    @Override
    public int getWidth() {
        return Math.min(getDesiredWidth(), mMaxWidth);
    }

    @Override
    public int getDesiredHeight() {
        return size;
    }

    @Override
    public int getDesiredWidth() {
        return size;
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
        if (textConfig != null) {
            textConfig.setMaxWidth(Math.min(maxWidth, size));
        }
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
        if (textConfig != null) {
            textConfig.setMaxHeight(Math.min(maxHeight, size));
        }
    }

    // TODO: 17/1/25 状态没改变时返回false
    @Override
    public boolean setState(int[] state) {
        return false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        int radius = size / 2;
        canvas.translate((getWidth() - size) / 2, (getHeight() - size) / 2);
        canvas.drawCircle(radius, radius, radius, mPaint);
        canvas.restore();
        if (textConfig != null) {
            canvas.save();
            int tLeft = Math.max(0, (getWidth() - textConfig.getWidth()) / 2);
            int tTop = Math.max(0, (getHeight() - textConfig.getHeight()) / 2);
            canvas.translate(tLeft, tTop);
            canvas.clipRect(0, 0, getWidth() - tLeft, getHeight() - tTop);
            textConfig.draw(canvas);
            canvas.restore();
        }
    }
}
