package flashbar.com.testapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.v4.view.ViewCompat;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Constants {

    public static String PREF_VALUE = "pref";
    public static final int PRIMARY_COLOR=Color.parseColor("#9C27B0");
    public static void Log(String message) {
        Log.e("vc music", message);
    }

    public static boolean hitTest(View v, int x, int y) {
        final int tx = (int) (ViewCompat.getTranslationX(v) + 0.5f);
        final int ty = (int) (ViewCompat.getTranslationY(v) + 0.5f);
        final int left = v.getLeft() + tx;
        final int right = v.getRight() + tx;
        final int top = v.getTop() + ty;
        final int bottom = v.getBottom() + ty;

        return (x >= left) && (x <= right) && (y >= top) && (y <= bottom);
    }

    public static Bitmap decodeBitmap(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static Bitmap decodeBitmap(File path) {
        return BitmapFactory.decodeFile(path.getPath());
    }

    public static Bitmap decodeBitmap(Resources resource, int resourceId) {
        return BitmapFactory.decodeResource(resource, resourceId);
    }

    public static Bitmap decodeScaledBitmap(String path, int width, int height) {
        Bitmap bitmap =BitmapFactory.decodeFile(path);
        if(bitmap!=null)
          bitmap=Bitmap.createScaledBitmap(bitmap,width,height,false);
        return bitmap;
    }

    public static Bitmap decodeScaledBitmap(File path, int width, int height) {
       Bitmap bitmap=BitmapFactory.decodeFile(path.getPath());
        if(bitmap!=null)
           bitmap=Bitmap.createScaledBitmap(bitmap, width, height, false);
        return bitmap;
    }

    public static Bitmap decodeScaledBitmap(Resources resources, int resId, int width, int height) {
        Bitmap bitmap=BitmapFactory.decodeResource(resources, resId);
        if(bitmap!=null)
            bitmap=Bitmap.createScaledBitmap(bitmap, width, height, false);
        return bitmap;
    }

    public static Bitmap decodeScaledBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

/*
    public static boolean isScanningServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Scanning.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isActivityRunning(Context context) {
        String myPackage=Scrolltabs.class.getPackage().getName();

        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        return componentInfo.getPackageName().equals(myPackage);
    }

    public static boolean isServiceRunning(Context context) {
        String serviceName = AudioService.class.getName();
        return baseCheckingService(context, serviceName);
    }

    public static boolean isFloatingWidgetServiceRunning(Context context){
        String serviceName = FloatingWidgetService.class.getName();
        return baseCheckingService(context,serviceName);
    }
*/

    private static boolean baseCheckingService(Context context,String serviceName) {

        if (context != null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(Integer.MAX_VALUE);
            Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();

            while (i.hasNext()) {
                ActivityManager.RunningServiceInfo runningServiceInfo = i.next();

                if (runningServiceInfo.service.getClassName().equals(serviceName)) {

                    if (runningServiceInfo.foreground)
                        return true;

                    break;
                }
            }
        }

        return false;
    }

    private static InputStream getInputStreamFromUrl(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        connection.setDoInput(true);
        connection.connect();

        return connection.getInputStream();
    }

    public static Bitmap getBitmapFromUrl(String path) throws IOException {

        InputStream inputStream = getInputStreamFromUrl(path);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(inputStream, null, options);

        if (options.outHeight >= 600 || options.outWidth >= 600)
            options.inSampleSize = 2;
        else
            options.inSampleSize = 1;

        options.inJustDecodeBounds = false;

        InputStream stream = getInputStreamFromUrl(path);

        Bitmap bmp = BitmapFactory.decodeStream(stream, null, options);

        inputStream.close();
        inputStream = null;
        stream.close();
        stream = null;

        return bmp;
    }

    public static void showMsg(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void showMsgVibrate(Context context, String string) {
        SharedPreferences auth = context.getSharedPreferences(Constants.PREF_VALUE, Context.MODE_MULTI_PROCESS);

        if (auth.getBoolean("vibrate", false)) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(300);
        }
        showMsg(context, string);
    }

    public static int getScreenWidth(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }



    public static ArrayList<String> splitWord(String string) {

        ArrayList<String> searchLetters = new ArrayList<String>();
        string = string.trim();

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ' || i == string.length() - 1) {
                if (i == string.length() - 1) {
                    if (string.charAt(i) != ' ')
                        searchLetters.add(string.substring(0, i + 1));
                    else
                        searchLetters.add(string.substring(0, i));

                    break;
                } else
                    searchLetters.add(string.substring(0, i));

                string = string.substring(i);

                string = string.trim();
                i = 0;
            }
        }

        return searchLetters;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        bmp2 = getResizedBitmap(bmp2, bmp1.getHeight(), bmp1.getWidth());
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }


    public static Bitmap imageGlow(Bitmap src) {
        int r = 0;
        int g = 0;
        int b = 0;
        try {
            int margin = 16;
            int halfMargin = margin / 2;
            int glowRadius = 12;
            int glowColor = Color.rgb(r, g, b);
            Bitmap alpha = src.extractAlpha();
            Bitmap bmp = Bitmap.createBitmap(src.getWidth() + margin, src.getHeight() + margin, Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            Paint paint = new Paint();
            paint.setColor(glowColor);
            paint.setMaskFilter(new BlurMaskFilter(glowRadius, Blur.OUTER));//For Inner glow set Blur.INNER
            canvas.drawBitmap(alpha, halfMargin, halfMargin, paint);
            canvas.drawBitmap(src, halfMargin, halfMargin, null);


            return bmp;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return src;
        }


    }


    public static Bitmap blurBitmap(Context context, Bitmap bitmap) {

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25.f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }

    public static Bitmap changeColor(Bitmap bitmap, int r, int g, int b) {
        Bitmap myBitmap;

        if (bitmap.isMutable())
            myBitmap = bitmap;
        else
            myBitmap = bitmap.copy(bitmap.getConfig(), true);

        int height = myBitmap.getHeight();
        int width = myBitmap.getWidth();
        int total = width * height;

        int[] allPixels = new int[total];

        myBitmap.getPixels(allPixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < total; i++) {

            if (allPixels[i] != Color.TRANSPARENT) {
                allPixels[i] = Color.argb(Color.alpha(allPixels[i]), r, g, b);
            }
        }
        myBitmap.setPixels(allPixels, 0, width, 0, 0, width, height);

        return myBitmap;
    }


    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap ;
        try
        {

            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            if (radius < 1)
            {
                return (null);
            }

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();


            int[] pix = new int[w * h];
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int dv[] = new int[256 * divsum];
            for (i = 0; i < 256 * divsum; i++)
            {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }

            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            sentBitmap=null;
            return (bitmap);

        }
        catch(Exception e)
        {
            bitmap=null;
            return sentBitmap;
        }

    }

    public static void writeBmpToFile(File filename, Bitmap bmp) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
