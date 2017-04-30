package study.pmoreira.builditbigger;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtils {

    private NetworkUtils() {
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
