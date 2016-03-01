package com.danialgoodwin.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SimpleStorage {

    /* Suppress default constructor for non-instantiability. */
    private SimpleStorage() { throw new AssertionError(); }

    /**
     * NOTE: This will overwrite any file with the same name
     * @param context
     * @param filename
     * @param text
     * @return file is successful, otherwise null
     */
    @SuppressLint({"SetWorldReadable"})
    @Nullable
    public static File newCacheFilePublicReadable(@NonNull Context context,
            @NonNull String filename, @NonNull String text) {
        File file = new File(context.getCacheDir(), filename);
        file.setReadable(true, false);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file, false);
            outputStream.write(text.getBytes());
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignore) {}
            }
        }
        return null;
    }


}
