package me.alvince.android.avatarimageview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.TypedValue;

/**
 * Created by alvince on 2018/4/18
 *
 * @author 杨小咩 alvince.zy@gmail.com
 */
class Utils {
    /**
     * Convert dimens from dip to px.
     */
    public static float fromDip(Context context, float dip) {
        Resources r = context != null ? context.getResources() : Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
    }

    /**
     * 将Drawable转换成带圆角的将Drawable
     *
     * @param drawable 原Drawable对象
     * @param outWidth 目标宽度
     * @param outHeight 目标高度
     * @param roundCorner 圆角大小
     * @return Drawable 带圆角的将Drawable
     */
    public static Drawable roundDrawable(@Nullable Drawable drawable, int outWidth, int outHeight, int roundCorner) {
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = Bitmap.createBitmap(((BitmapDrawable) drawable).getBitmap());
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        // 根据控件大小对纹理图进行拉伸缩放处理
        float widthScale = outWidth * 1.0f / bitmap.getWidth();
        float heightScale = outHeight * 1.0f / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapShader.setLocalMatrix(matrix);

        // 利用画笔将纹理图绘制到画布上面
        Bitmap targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);
        RectF outerRect = new RectF(0, 0, outWidth, outHeight);
        targetCanvas.drawRoundRect(outerRect, roundCorner, roundCorner, paint);

        return new BitmapDrawable(targetBitmap);
    }
}
