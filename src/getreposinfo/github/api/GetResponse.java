package getreposinfo.github.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class GetResponse {

	private String url = "";

	public GetResponse(String url) {
		this.url = url;
	}

	public String getJsonString() throws Exception {
		InputStream inputStream = new URL(this.url).openStream();

		BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
		String jsonString = this.getJsonString(bReader);

		bReader.close();

		return jsonString;
	}

	private String getJsonString(BufferedReader bReader) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();

		String line = "";
		while ((line = bReader.readLine()) != null) {
			stringBuilder.append(line);
		}

		return stringBuilder.toString();
	}
}
