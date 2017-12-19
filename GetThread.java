import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetThread implements Runnable
{
    private final Gson gson;
    private int n;
    private String login;

    public GetThread(String login)
    {
        this.login = login;
        gson = new GsonBuilder().create();
    }

    @Override
    public void run()
    {
        try {
            while ( ! Thread.interrupted())
            {
                URL url = new URL(Utils.getURL() + "/get?from=" + n);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                try {
                    byte[] buf = requestBodyToArray(inputStream);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                    if (list != null) {
                        for (Message message : list.getList())
                        {
                            if (message.getTo().equals("All") || message.getTo().equals(login)) {
                                System.out.println(message);
                            }
                            n++;
                        }
                    }
                }
                finally { inputStream.close(); }
                Thread.sleep(500);
            }
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    private byte[] requestBodyToArray(InputStream is) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}
