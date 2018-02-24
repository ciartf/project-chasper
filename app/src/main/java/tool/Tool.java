package tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fasterxml.uuid.Generators;

/**
 * Created by danu on 6/1/17.
 */

public class Tool {
    public static boolean isInternetConnected(Context ct) {

        try {
            final ConnectivityManager cm = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null) {
                    return networkInfo.isConnected();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getUUID() {
        String uuid = Generators.timeBasedGenerator().generate().toString().toUpperCase();
        return uuid;
    }
}
