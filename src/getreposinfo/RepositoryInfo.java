package getreposinfo;

import org.json.simple.JSONObject;

import getreposinfo.github.api.BranchesCount;
import getreposinfo.github.api.CommitCount;
import getreposinfo.github.api.ContributorsCount;
import getreposinfo.github.api.ReleasesCount;

public class RepositoryInfo {

	private static boolean ALLOW_FORKED = false;

	// default fields
	private static final String FORKED = "fork";
	private static final String ID = "id";
	private static final String FULL_NAME = "full_name";
	private static final String HTML_URL = "html_url";
	private static final String STARS_COUNT = "stargazers_count";
	private static final String WATCHERS_COUNT = "watchers_count";
	private static final String FORKS_COUNT = "forks_count";

	private long id = -1;
	private String full_name = "";
	private String htmlUrl = "";
	private long starsCount = -1;
	private long watchersCount = -1;
	private long commitCount = -1;
	private long releaseCount = -1;
	private long branchesCount = -1;
	private long forksCount = -1;
	private long contributorsCount = -1;
	// TODO implement using metricminer
	private long javaArchiveCount = -1;
	// TODO implement using metricminer
	private long lineCount = -1;

	private JSONObject jsonObject = null;

	private RepositoryInfo(JSONObject object) {
		this.jsonObject = object;
	}

	public static RepositoryInfo getInstance(JSONObject jsonObject) {

		if (ALLOW_FORKED == false && isForked(jsonObject))
			return null;

		RepositoryInfo repositoryInfo = new RepositoryInfo(jsonObject);

		try {
			setBasicInfo(repositoryInfo);
		} catch (Exception e) {
			return null;
		}

		setMainCounters(repositoryInfo);
		setCommitCount(repositoryInfo);
		setReleasesCount(repositoryInfo);
		setBranchesCount(repositoryInfo);
		setContributorsCount(repositoryInfo);

		return repositoryInfo;
	}

	private static void setContributorsCount(RepositoryInfo repositoryInfo) {
		ContributorsCount count = new ContributorsCount(repositoryInfo.full_name);
		repositoryInfo.contributorsCount = count.getTotal();
	}

	private static void setBranchesCount(RepositoryInfo repositoryInfo) {
		BranchesCount branchesCount = new BranchesCount(repositoryInfo.full_name);
		repositoryInfo.branchesCount = branchesCount.getTotal();
	}

	private static void setReleasesCount(RepositoryInfo repositoryInfo) {
		ReleasesCount releasesCount = new ReleasesCount(repositoryInfo.full_name);
		repositoryInfo.releaseCount = releasesCount.getTotal();
	}

	private static void setCommitCount(RepositoryInfo repositoryInfo) {
		CommitCount commitCount = new CommitCount(repositoryInfo.full_name);
		repositoryInfo.commitCount = commitCount.getTotal();
	}

	private static void setMainCounters(RepositoryInfo repositoryInfo) {
		Object startsObj = repositoryInfo.jsonObject.get(STARS_COUNT);
		Object watchersObj = repositoryInfo.jsonObject.get(WATCHERS_COUNT);
		Object forksObj = repositoryInfo.jsonObject.get(FORKS_COUNT);

		repositoryInfo.starsCount = (startsObj == null ? -1 : (Long) startsObj);
		repositoryInfo.watchersCount = (watchersObj == null ? -1 : (Long) watchersObj);
		repositoryInfo.forksCount = (forksObj == null ? -1 : (Long) forksObj);
	}

	private static void setBasicInfo(RepositoryInfo repositoryInfo) throws Exception {
		repositoryInfo.id = (Long) repositoryInfo.jsonObject.get(ID);
		repositoryInfo.full_name = (String) repositoryInfo.jsonObject.get(FULL_NAME);
		repositoryInfo.htmlUrl = (String) repositoryInfo.jsonObject.get(HTML_URL);
	}

	private static boolean isForked(JSONObject jsonObject) {
		Object obj = jsonObject.get(FORKED);
		return (obj == null ? false : (Boolean) obj);
	}
	
	public long getId() {
		return id;
	}

	public String getFull_name() {
		return full_name;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public long getStarsCount() {
		return starsCount;
	}

	public long getWatchersCount() {
		return watchersCount;
	}

	public long getCommitCount() {
		return commitCount;
	}

	public long getReleaseCount() {
		return releaseCount;
	}

	public long getBranchesCount() {
		return branchesCount;
	}

	public long getForksCount() {
		return forksCount;
	}

	public long getContributorsCount() {
		return contributorsCount;
	}

	public long getJavaArchiveCount() {
		return javaArchiveCount;
	}

	public long getLineCount() {
		return lineCount;
	}
}
