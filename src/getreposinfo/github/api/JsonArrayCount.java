package getreposinfo.github.api;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public abstract class JsonArrayCount {

	private static final String PAGE_PATTERN = "?per_page=100&page=";

	protected String url = "";

	public JsonArrayCount(String fullNameRepo) {
		this.url = this.buildUrl(fullNameRepo);
	}

	protected abstract String buildUrl(String fullNameRepo);

	public long getTotal() {
		int totalSize = 0;
		boolean exit = false;
		int page = 1;
		
		while (exit == false) {
			try {
				String jsonString = (new GetResponse(this.url + PAGE_PATTERN + page)).getJsonString();
				JSONArray jsonArray = (JSONArray) (new JSONParser()).parse(jsonString);
				
				if (jsonArray.isEmpty()) {
					exit = true;
				} else {
					totalSize += jsonArray.size();
					++page;
				}
			} catch (Exception e) {
				System.out.println("request error");
			}
		}

		return totalSize;
	}
}
