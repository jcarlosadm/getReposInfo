package getreposinfo.github.api;

public class ReleasesCount extends JsonArrayCount {

	private static final String PRE_URL = "https://api.github.com/repos/";
	private static final String POST_URL = "/releases";
	
	public ReleasesCount(String fullNameRepo) {
		super(fullNameRepo);
	}

	@Override
	protected String buildUrl(String fullNameRepo) {
		return PRE_URL + fullNameRepo + POST_URL;
	}
}
