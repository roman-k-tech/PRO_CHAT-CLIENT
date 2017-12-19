import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
    {
        String  login = null;
        String password;
        boolean authorized = false;

		try (Scanner scanner = new Scanner(System.in)) {
            for (int i = 3; i > 0; i--)
            {
                System.out.print("Enter your LOGIN: ");
                login = scanner.nextLine();
                System.out.print("Enter your PASSWORD: ");
                password = scanner.nextLine();

                URL url = new URL(Utils.getURL() + "/auth?login=" + login + "&password=" + password);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setRequestProperty("login", login); //??????
//                httpURLConnection.setRequestProperty("password", password);

                int result = httpURLConnection.getResponseCode();
                httpURLConnection.disconnect();
                System.out.println(result);
                if (result == 202) {
                    System.out.println("You are logged in as " + login);
                    authorized = true;
                    break;
                }
                else if (result == 401) {
                    System.out.println("LOGIN OR PASSWORD IS INCORECT!");
                }
            }
            if (! authorized) {
                System.out.println("CLOSING PROGRAM.");
                return;
            }

			Thread th = new Thread(new GetThread(login));
			th.setDaemon(true);
			th.start();

            System.out.println("Type '/help' for help. Enter your message: ");
			while (true)
            {
				String text = scanner.nextLine();
				if (text.isEmpty()) {}
                else if (text.equals("/list") || text.startsWith("/status") || text.equals("/help"))
                    RequestSomething.request(text);
				else if (text.equals("/exit")) {
                    RequestSomething.request(text);
				    break;
                }
                else {
                    Message message;
                    if (text.startsWith("/private")) {
                        String words[] = text.split(" ");
                        System.out.print("Type in private message to " + words[1] + ": ");
                        text = scanner.nextLine();
                        message = new Message(login, text);
                        message.setTo(words[1]);
                    }
                    else {
                        message = new Message(login, text);
                    }
                    message.setAuthorized(authorized);
                    int res = message.send(Utils.getURL() + "/add");
                    if (res == 404) {
                        System.out.println("User does not exist: " + res);
                    }
                    else if (res != 200) { // 200 OK
                        System.out.println("HTTP error occured: " + res);
                        break;
                    }
                }
			}
			scanner.close();
		}
		catch (IOException ex) { ex.printStackTrace(); }
	}
}
