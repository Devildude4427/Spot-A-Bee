package com.assignment.spotabee.customutils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;



public class FileOp {

    private static final String TAG = "FileOPDebug";


    @Nullable
    public static byte[] getByteArrayFromIntentData(@NonNull Context context, @NonNull Intent data) {
        InputStream inStream = null;
        Bitmap bitmap = null;
        try {
            inStream = context.getContentResolver().openInputStream(data.getData());
            Log.v(TAG, "Instream works");
            bitmap = BitmapFactory.decodeStream(inStream);
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            return outStream.toByteArray();
        } catch (FileNotFoundException e) {
            Log.v(TAG, "Exception " + e);
            return null;
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    @Nullable
    public static byte[] getByteArrayFromIntentStoreData(@NonNull Context context, @NonNull Intent data) {
        InputStream inStream = null;
        Bitmap bitmap = null;
        try {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

            return outStream.toByteArray();
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
            return null;
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }
}

