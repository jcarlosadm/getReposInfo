package getreposinfo;

public class RepositoryInfo {

	private String id = "";
	private String htmlUrl = "";
	private int starsCount = -1;
	private int watchersCount = -1;
	private int commitCount = -1;
	private int releaseCount = -1;
	private int branchesCount = -1;
	private int forksCount = -1;
	private int contributorsCount = -1;
	private int javaArchiveCount = -1;
	private int lineCount = -1;

	public void setId(String id) {
		this.id = id;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public void setStarsCount(int starsCount) {
		this.starsCount = starsCount;
	}

	public void setWatchersCount(int watchersCount) {
		this.watchersCount = watchersCount;
	}

	public void setCommitCount(int commitCount) {
		this.commitCount = commitCount;
	}

	public void setReleaseCount(int releaseCount) {
		this.releaseCount = releaseCount;
	}

	public void setBranchesCount(int branchesCount) {
		this.branchesCount = branchesCount;
	}

	public void setForksCount(int forksCount) {
		this.forksCount = forksCount;
	}

	public void setContributorsCount(int contributorsCount) {
		this.contributorsCount = contributorsCount;
	}

	public void setJavaArchiveCount(int javaArchiveCount) {
		this.javaArchiveCount = javaArchiveCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public String getCsvLine() {
		StringBuilder csvLine = new StringBuilder();

		csvLine.append(this.id + ",");
		csvLine.append(this.htmlUrl + ",");
		csvLine.append(this.commitCount + ",");
		csvLine.append(this.javaArchiveCount + ",");
		csvLine.append(this.lineCount + ",");
		csvLine.append(this.watchersCount + ",");
		csvLine.append(this.starsCount + ",");
		csvLine.append(this.forksCount + ",");
		csvLine.append(this.branchesCount + ",");
		csvLine.append(this.contributorsCount + ",");
		csvLine.append(this.releaseCount + ",");

		return csvLine.toString();
	}

	public static String getHeader() {
		return "ID,url,number of commits,number of Java files,max number of lines,watchers,"
				+ "stars,forks,branches,contributors,releases";
	}

}
