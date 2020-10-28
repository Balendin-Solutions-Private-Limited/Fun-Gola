package com.thambola.fungola.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thambola.fungola.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.thambola.fungola.Utils.RijndaelCrypt.TAG;

/**
 * Created on : June 18, 2016
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public class ImageUtil {

    public static String imagepath;
    private static File destFile;
    private static Context context1;

    private ImageUtil() {

    }

    public static File CompressImage(File imageFile, int reqWidth, int reqHeight, int quality, String destinationPath) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destinationPath);
            // write the compressed bitmap at the destination specified by destinationPath.
            decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight).compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        return new File(destinationPath);
    }

    static Bitmap decodeSampledBitmapFromFile(File imageFile, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String EncodeImageToString(ImageView imgPicture) {
        Bitmap bm = ((BitmapDrawable) imgPicture.getDrawable()).getBitmap();;
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(scaleImage(bm, Bitmap.CompressFormat.JPEG), Base64.DEFAULT);
    }

    public static void onCaptureImageResult(Intent data, ImageView imgPicture) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        imgPicture.setImageBitmap(thumbnail);
    }

    private static Bitmap decodeFile(File f) {

        /*File file = new File(Environment.getExternalStorageDirectory()
                + "/" + Constants.appname);*/

        File file = new File((context1
                .getApplicationContext().getFileStreamPath(""+context1.getResources().getString(R.string.app_name))
                .getPath()));

        System.out.println("rrrrrrrrrrr file: "+file.getAbsolutePath());
        if (!file.exists()) {
            file.mkdirs();
        }

        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());

        destFile = new File(file, "img_"
                + System.currentTimeMillis() + ".png");
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            b.compress(Bitmap.CompressFormat.PNG, 50, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        imagepath=destFile.getAbsolutePath();
        System.out.println("rrrrrrrrrrrr imagepath "+imagepath);
        return b;
    }
    public static void onSelectFromGalleryResult(Intent data, ImageView imgPicture, Context context) {

        context1=context;
        if (data != null) {
            try {

                imgPicture.setImageBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData()));

                 imagepath=getRealPathFromURI(context,data.getData());

                File sourceFile = new File(imagepath);
                //File newfile=CompressImage(sourceFile,150,150,Bitmap.CompressFormat.PNG,100,sourceFile.getAbsolutePath());
               // File newfile=CompressImage(sourceFile,150,150,100,sourceFile.getAbsolutePath());

                decodeFile(sourceFile);

                System.out.println("rrrrrrrrrrrrrr imagepath:::  "+imagepath+" destFile "+destFile.getAbsolutePath());
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher);
                Glide.with(context).load(""+imagepath)
                        .apply(options).into(imgPicture);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("rrrrrrrrrrrr error getting image path "+e.getMessage());
            }
        }
    }

    private static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);
        } catch (Exception e) {
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    public static void SetImage(String imageBytes, ImageView imgView)
    {
        byte[] decodedString = Base64.decode(imageBytes, Base64.DEFAULT);
        imgView.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
    }


    public static boolean askForPermission(String permission, Integer requestCode, Activity context) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
            }
            return false;
        } else {
            Toast.makeText(context, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            return true;
        }
    }




    private static int MAX_IMAGE_DIMENSION = 750;

    private static byte[] scaleImage(Bitmap bitmap, Bitmap.CompressFormat format) {

        int width = bitmap.getWidth();
        //Log.v("bitmap.getWidth()",String.valueOf(width));

        if(width > MAX_IMAGE_DIMENSION) {
            float ratio = (float) MAX_IMAGE_DIMENSION / (float) width;

            width = MAX_IMAGE_DIMENSION;
            int height = (int)((float)bitmap.getHeight() * ratio);
          //  bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(format,100, stream);

        //Log.v("bitmap.getWidth()",String.valueOf(stream.toByteArray().length));
        return stream.toByteArray();
    }


}
