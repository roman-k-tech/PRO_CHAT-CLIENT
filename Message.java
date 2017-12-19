import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Message
{
	private Date date = new Date();
	private String login;
	private String password;
	private boolean authorized = false;
	private String to = "All";
	private String text;

	public Message(String login, String text)
	{
		this.login = login;
		this.text = text;
	}

	public String toJSON()
	{
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

	public static Message fromJSON(String s)
	{
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(s, Message.class);
	}

	@Override
	public String toString()
	{
		return new StringBuilder().append("[").append(date)
				.append(", From: ").append(login).append(", To: ").append(to)
				.append("] ").append(text)
				.toString();
	}

	public int send(String url) throws IOException
	{
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

		conn.setRequestMethod("POST");
		conn.setDoOutput(true);

		OutputStream os = conn.getOutputStream();
		try {
			String json = toJSON();
			os.write(json.getBytes(StandardCharsets.UTF_8));
			return conn.getResponseCode();
		}
		finally { os.close(); }
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getFrom() {
		return login;
	}
	public void setFrom(String from) {
		this.login = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLogin()
	{
		return login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAuthorized() {
		return authorized;
	}
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}
}
