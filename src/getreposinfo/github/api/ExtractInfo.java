package getreposinfo.github.api;

import java.util.Objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import getreposinfo.util.GetResponse;

public class ExtractInfo {

	private static final String ID = "id";
	private static final String STARS_COUNT = "stargazers_count";
	private static final String WATCHERS_COUNT = "watchers_count";
	private static final String FORKS_COUNT = "forks_count";

	private static final int NUMBER_OF_TENTATIVES = 361;
	private static final int TIME_IN_MILIS = 10000;
	private static final String PRE_URL = "https://api.github.com/repos";

	private String userName = "";
	private String repositoryName = "";

	private JSONObject basicJson = null;

	public ExtractInfo(String userName, String repositoryName) throws Exception {
		this.userName = userName;
		this.repositoryName = repositoryName;

		int count = 0;
		while (count < NUMBER_OF_TENTATIVES) {
			this.getBasicJson();
			if (this.basicJson != null) {
				break;
			} else {
				Thread.sleep(TIME_IN_MILIS);
				System.out.println(userName + "/" + repositoryName + ": error to get info (tentative " + (count + 1)
						+ " of " + NUMBER_OF_TENTATIVES + "). trying again");
			}

			++count;
		}

		// TODO customize exception
		if (this.basicJson == null) {
			throw new Exception();
		}
	}

	public String getID() {
		Long idLong = (Long) this.basicJson.get(ID);
		return Objects.toString(idLong, null);
	}

	public int getNumberOfStars() {
		return getNumberOfObjectOfBasicJson(STARS_COUNT);
	}

	public int getNumberOfForks() {
		return getNumberOfObjectOfBasicJson(FORKS_COUNT);
	}

	public int getNumberOfWatchers() {
		return getNumberOfObjectOfBasicJson(WATCHERS_COUNT);
	}

	private int getNumberOfObjectOfBasicJson(String field) {
		Long objectLong = (Long) this.basicJson.get(field);
		return (objectLong == null ? 0 : objectLong.intValue());
	}

	public int getNumberOfReleases() {
		return this.getNumberOfObjectOfOtherRequest(new ReleasesCount(this.userName + "/" + this.repositoryName));
	}

	public int getNumberOfBranches() {
		return this.getNumberOfObjectOfOtherRequest(new BranchesCount(this.userName + "/" + this.repositoryName));
	}

	public int getNumberOfDevs() {
		return this.getNumberOfObjectOfOtherRequest(new ContributorsCount(this.userName + "/" + this.repositoryName));
	}

	private int getNumberOfObjectOfOtherRequest(JsonArrayCount jsonArrayCount) {
		Long longValue = (Long) jsonArrayCount.getTotal();
		return (longValue == null ? 0 : longValue.intValue());
	}

	private void getBasicJson() {
		String url = PRE_URL + "/" + this.userName + "/" + this.repositoryName;
		GetResponse getResponse = new GetResponse(url);

		String jsonString = "";
		try {
			jsonString = getResponse.getJsonString();
		} catch (Exception e) {
			jsonString = "";
		}
		if (jsonString == null || jsonString.isEmpty()) {
			return;
		}

		try {
			this.basicJson = (JSONObject) (new JSONParser().parse(jsonString));
		} catch (ParseException e) {
			this.basicJson = null;
		}
	}
	
	// TODO remove later
	public static void main(String[] args) {
		try {
			ExtractInfo eInfo = new ExtractInfo("cSploit", "android");
			System.out.println("id: "+eInfo.getID());
			System.out.println("branches: "+eInfo.getNumberOfBranches());
			System.out.println("devs: "+eInfo.getNumberOfDevs());
			System.out.println("forks: "+eInfo.getNumberOfForks());
			System.out.println("releases: "+eInfo.getNumberOfReleases());
			System.out.println("stars: "+eInfo.getNumberOfStars());
			System.out.println("watchers: "+eInfo.getNumberOfWatchers());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
