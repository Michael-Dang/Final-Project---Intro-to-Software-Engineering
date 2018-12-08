package ceg4110_f18_g19.isitfood.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

public class ResponseDecoder {
    public static String[] pairize(String[] results)
    {
        if((results.length-1)%2 != 0)
        {
            return null;
        }
        String[] ret = new String[(results.length-1)/2];
        for(int i = 0, j = 1; i < ret.length; i++)
        {
            ret[i] = new StringBuilder().append(results[j++]).append(" ").append(results[j++]).toString();
        }
        return ret;
    }
    public static void decode(Activity caller, String response, Parcelable... stuff)
    {
        String[] results = response.split(" ");
        if(results[0].equals("NET:")) {
            results = pairize(results);
            Intent[] intents = new Intent[results.length];
            for(int i=0; i< results.length; i++) {
                intents[i] = new Intent(caller, ImageViewActivity.class);
                intents[i].putExtra("ceg4110_f18_g19.isitfood.BITMAP", stuff[i]);
                intents[i].putExtra("ceg4110_f18_g19.isitfood.RESULT", results[i]);
            }
            caller.startActivities(intents);
        }
    }
}
