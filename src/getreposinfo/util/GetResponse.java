package getreposinfo.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class GetResponse {

	private String url = "";

	public GetResponse(String url) {
		String token = PropertiesManager.getProperty("github.token");
		if (token != null && !token.isEmpty()) {
			if (url.contains("?"))
				url += "&";
			else
				url += "?";
			url += "access_token=" + token;
		}
		
		this.url = url;
	}

	public String getJsonString() throws Exception {
		InputStream inputStream = new URL(this.url).openStream();

		BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
		String jsonString = this.buildJsonString(bReader);

		bReader.close();

		return jsonString;
	}

	private String buildJsonString(BufferedReader bReader) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();

		String line = "";
		while ((line = bReader.readLine()) != null) {
			stringBuilder.append(line);
		}

		return stringBuilder.toString();
	}
}
