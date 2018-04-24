package me.alvince.android.avatarimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆角头像 {@link ImageView}
 * <p/>
 * Created by alvince on 2018/1/23.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0, 2018/4/24
 */
public class AvatarImageView extends ImageView {

    private static final String TAG = "AvatarImageView";
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    private static final int DEFAULT_STROKE_COLOR = Color.TRANSPARENT;
    private static final int DEFAULT_STROKE_WIDTH = 2;  // with unit dip
    private static final int DEFAULT_CORNER_RADIUS = 5;  // with unit dip

    private Paint mImagePaint;
    private Paint mStrokePaint;
    private Paint mForegroundPaint;
    private RectF mImageShadeRect;
    private RectF mStrokeRoundRect;
    private RectF mForegroundRect;
    private BitmapShader mImageShader;

    private boolean rearrangeImage;
    private int colorPressed;
    private int roundCorner;
    private int strokeColor;
    private int strokeWidth;

    public AvatarImageView(@NonNull Context context) {
        this(context, null);
    }

    public AvatarImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        rearrangeImage = true;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView, defStyleAttr, 0);
        rearrangeImage = a.getBoolean(R.styleable.AvatarImageView_img_drawImageReplace, true);
        colorPressed = a.getColor(R.styleable.AvatarImageView_img_foregroundColorPressed, Color.TRANSPARENT);
        roundCorner = a.getDimensionPixelSize(R.styleable.AvatarImageView_img_roundCorner, (int) Utils.fromDip(context, DEFAULT_CORNER_RADIUS));
        strokeColor = a.getColor(R.styleable.AvatarImageView_img_strokeColor, DEFAULT_STROKE_COLOR);
        strokeWidth = a.getDimensionPixelSize(R.styleable.AvatarImageView_img_strokeWidth, (int) Utils.fromDip(context, DEFAULT_STROKE_WIDTH));
        a.recycle();

        mForegroundRect = new RectF();
        mForegroundPaint = new Paint();
        mForegroundPaint.setStyle(Paint.Style.FILL);
        mForegroundPaint.setColor(colorPressed);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setup();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setup();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        setup();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw image
        if (rearrangeImage && roundCorner > 0 && mImagePaint != null) {
            canvas.drawRoundRect(mImageShadeRect, roundCorner, roundCorner, mImagePaint);
        } else {
            super.onDraw(canvas);
        }
        // draw stroke rect
        if (strokeColor != Color.TRANSPARENT && strokeWidth > 0
                && mStrokePaint != null && mStrokeRoundRect != null) {
            canvas.drawRoundRect(mStrokeRoundRect, roundCorner, roundCorner, mStrokePaint);
        }
        // draw pressed mask foreground color
        if (isPressed() && colorPressed != Color.TRANSPARENT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            canvas.drawRoundRect(mForegroundRect, roundCorner, roundCorner, mForegroundPaint);
        }
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        // use #setForeground instead while above Android-Marshmallow
        if (colorPressed != 0 && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            invalidate();
        }
    }

    /**
     * Setup image round corner
     *
     * @param corner round corner in pixel
     */
    public void setRoundCorner(int corner) {
        roundCorner = Math.max(0, corner);
        setup();
    }

    /**
     * Setup stroke color
     *
     * @param strokeColor stroke color int
     */
    public void setStrokeColor(@ColorInt int strokeColor) {
        this.strokeColor = strokeColor;
        setup();
    }

    /**
     * Setup stroke width
     *
     * @param width stroke size in pixel
     */
    public void setStrokeWidth(int width) {
        strokeWidth = Math.max(0, width);
        setup();
    }

    /**
     * Enable custom drawing image
     *
     * @param enable true custom drawing, else platform default
     */
    public void enableRenderOver(boolean enable) {
        rearrangeImage = enable;
        postInvalidate();
    }

    /**
     * Setup foreground pressed
     * <p>
     * Use {@link #setForeground(Drawable)} instead while above Android-Marshmallow
     *
     * @param foreground foreground color pressed
     */
    public void setColorPressed(@ColorInt int foreground) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return;
        }
        colorPressed = foreground;
        postInvalidate();
    }

    private Bitmap getBitmap() {
        Drawable drawable = getDrawable();

        if (drawable == null) return null;

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setup() {
        int w = getWidth();
        int h = getHeight();
        if (w == 0 && h == 0) {
            return;
        }

        Bitmap bitmap = getBitmap();
        if (bitmap == null) {
            return;
        }

        // config foreground bound
        Rect paddingRect = new Rect();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            paddingRect.set(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getPaddingBottom());
        } else {
            paddingRect.set(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        mForegroundRect.set(paddingRect.left, paddingRect.top, w - paddingRect.right, h - paddingRect.bottom);

        if (roundCorner == 0 && (strokeWidth == 0 || strokeColor == Color.TRANSPARENT)) return;
        final float renderOffset = strokeWidth * .5F;

        // config stroke paint & rect
        if (strokeColor != Color.TRANSPARENT && strokeWidth > 0) {
            if (mStrokePaint == null) {
                mStrokePaint = new Paint();
                mStrokePaint.setAntiAlias(true);
            }
            mStrokePaint.setStyle(Paint.Style.STROKE);
            mStrokePaint.setColor(strokeColor);
            mStrokePaint.setStrokeWidth(strokeWidth);

            float strokeL, strokeT = strokeL = renderOffset;
            float strokeR = w - renderOffset;
            float strokeB = h - renderOffset;
            if (mStrokeRoundRect == null) {
                mStrokeRoundRect = new RectF(strokeL, strokeT, strokeR, strokeB);
            } else {
                mStrokeRoundRect.set(strokeL, strokeT, strokeR, strokeB);
            }
        }

        // config image shader
        if (rearrangeImage) {
            float bitmapWidth = bitmap.getWidth();
            float bitmapHeight = bitmap.getHeight();
            if (bitmapWidth == 0 || bitmapHeight == 0) return;

            RectF imageRect = new RectF(paddingRect.left + renderOffset, paddingRect.top + renderOffset,
                    w - paddingRect.right - renderOffset, h - paddingRect.bottom - renderOffset);
            mImageShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            float availableW = w - paddingRect.left - paddingRect.right - strokeWidth * 2F;
            float availableH = h - paddingRect.top - paddingRect.bottom - strokeWidth * 2F;
            float ratioX = availableW / bitmapWidth;
            float ratioY = availableH / bitmapHeight;
            float scale = Math.max(ratioX, ratioY);
            float dx = (w - bitmapWidth * scale) * .5F;
            float dy = (h - bitmapHeight * scale) * .5F;
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);
            mImageShader.setLocalMatrix(matrix);

            // config image paint & shade rect
            if (mImagePaint == null) {
                mImagePaint = new Paint();
                mImagePaint.setAntiAlias(true);
            }
            mImagePaint.setShader(mImageShader);
            if (mImageShadeRect == null) {
                mImageShadeRect = new RectF(imageRect);
            } else {
                mImageShadeRect.set(imageRect);
            }
        }

        if (getParent() == null) {
            return;
        }

        if (Utils.isCurrUiThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}
