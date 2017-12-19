import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RequestSomething
{
//    String request;
    private static final Gson gson = new GsonBuilder().create();

    public RequestSomething (String request)
    {
  //      this.request = request;
    }

    static public void request (String request) throws IOException
    {
        if (request.equals("/list"))
        {
            URL url = new URL(Utils.getURL() + "/auth?list");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);

            InputStream inputStream = httpURLConnection.getInputStream();

            byte[] buf = requestBodyToArray(inputStream);
            String strBuf = new String(buf, StandardCharsets.UTF_8);

            ArrayList<String> list = gson.fromJson(strBuf, ArrayList.class);
            System.out.println("CONNECTED USERS LIST:");
            list.stream().forEach(a -> System.out.println(a));
            httpURLConnection.disconnect();
            inputStream.close();
        }
        else if (request.startsWith("/status"))
        {
            String words[] = request.split(" ");
            if ((words.length == 2) && ("online".equals(words[1].toLowerCase()) || "away".equals(words[1].toLowerCase()) || "dnd".equals(words[1].toLowerCase())))
            {
                URL url = new URL(Utils.getURL() + "/auth?status=" + words[1]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);

                int result = httpURLConnection.getResponseCode();
                System.out.println("Status set.");
                httpURLConnection.disconnect();
            }
            else
            {
                System.out.println("INVALID STATUS!");
            }
        }
        else if (request.equals("/exit"))
        {
            URL url = new URL(Utils.getURL() + "/auth?exit");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);

            int result = httpURLConnection.getResponseCode();
            System.out.println("Leaving chat.");
            httpURLConnection.disconnect();
        }
        else if (request.startsWith("/status"))
        {
            String words[] = request.split(" ");
            URL url = new URL(Utils.getURL() + "/add?private=" + words[1]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);

            int result = httpURLConnection.getResponseCode();
            System.out.println("Status set.");
            httpURLConnection.disconnect();

        }

        else if (request.equals("/help"))
        {
            System.out.println("COMMANDS ARE:");
            System.out.println("/help - List of options.");
            System.out.println("/list - List of online users.");
            System.out.println("/status 'option' - Set status. Available options are 'online', 'away', 'dnd'.");
            System.out.println("/private 'option' - Send private message. Must contain a valid name of user as an option.");
            System.out.println("/exit - Exits Chat.");
        }
    }


    static private byte[] requestBodyToArray(InputStream is) throws IOException
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
