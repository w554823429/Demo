package com.example.demo.util;

import java.io.Closeable;
import java.io.IOException;

import android.database.Cursor;

public class CloseHelper {
	public static void close(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }


    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {

        }
    }
}
