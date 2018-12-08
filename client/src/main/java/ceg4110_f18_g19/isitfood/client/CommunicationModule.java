package ceg4110_f18_g19.isitfood.client;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommunicationModule {
    public static ArrayList<String> encode(List<InputStream> files)
    {
        ArrayList<String> body = new ArrayList<>();
        for(InputStream f: files)
        {
            StringBuilder entry = new StringBuilder();
            entry.append("Content-Disposition: form-data; name=\"b64image\"");
            entry.append("\n");
            FileInputStream fileInputStreamReader = null;
            try
            {
                byte[] fileBytes = new byte[f.available()];
                f.read(fileBytes);
                entry.append(Base64.encodeToString(fileBytes, Base64.DEFAULT));
                body.add(entry.toString());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return body;
    }
    public static String request(List<String> body)
    {
        try
        {
            String boundary = Long.toHexString(System.currentTimeMillis());
            URL url = new URL(ServerConfiguration.getInstance().getURL() + "/analyze");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            PrintWriter writer = null;
            try
            {
                writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
                for(String entry: body)
                {
                    writer.println("--" + boundary);
                    writer.println(entry);
                }
                writer.println("--" + boundary + "--");

                InputStream response = connection.getInputStream();
                byte[] responseBytes = new byte[response.available()];
                response.read(responseBytes);
                return new String(responseBytes);
            }
            finally
            {
                if (writer != null) writer.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "No Response";
    }
}
