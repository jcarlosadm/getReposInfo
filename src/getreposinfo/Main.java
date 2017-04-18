package getreposinfo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.repodriller.RepoDriller;

import getreposinfo.github.api.ExtractInfo;
import getreposinfo.repodriller.ExploreRepo;
import getreposinfo.repodriller.JavaParserVisitor;
import getreposinfo.util.PropertiesManager;
import getreposinfo.util.Repository;
import getreposinfo.util.file.CSVBuilder;
import getreposinfo.util.file.Folders;

public class Main {

	private static boolean CLONE_REPOS = true;

	public static void main(String[] args) {
		DOMConfigurator.configure("log4j.xml");

		if (!Folders.makeFolders()) {
			System.out.println("error to make folder");
			return;
		}

		// TODO test if github token is set

		CSVBuilder csvBuilder = null;
		try {
			csvBuilder = new CSVBuilder(new File(Folders.OUTPUTS_FOLDER + File.separator + "results.csv"));
			csvBuilder.write(RepositoryInfo.getHeader());
		} catch (Exception e1) {
			System.out.println("error to create csv file");
			return;
		}

		JavaParserVisitor javaParserVisitor = new JavaParserVisitor();

		List<String> urls = getUrls();
		if (urls == null) {
			System.out.println("error to get urls");
			return;
		}

		for (String urlProject : urls) {
			if (CLONE_REPOS && !Repository.cloneRepo(urlProject)) {
				System.out.println("error to clone repository " + urlProject);
				continue;
			}

			String username = urlProject.substring(0, urlProject.lastIndexOf("/"));
			username = username.substring(username.lastIndexOf("/") + 1);

			String reponame = urlProject.substring(urlProject.lastIndexOf("/") + 1);
			if (reponame.endsWith(".git"))
				reponame = reponame.substring(0, reponame.lastIndexOf("."));
			System.out.println("project: " + reponame);

			System.out.println("Running repodriller");
			new RepoDriller().start(new ExploreRepo(javaParserVisitor, reponame));

			System.out.println("Running github api");
			ExtractInfo extractInfo = null;
			try {
				extractInfo = new ExtractInfo(username, reponame);
			} catch (Exception e) {
				System.out.println("error to extract information using github api");
				continue;
			}

			RepositoryInfo reInfo = new RepositoryInfo();
			reInfo.setLineCount(javaParserVisitor.getJavaLineCounter());
			reInfo.setJavaArchiveCount(javaParserVisitor.getNumberOfFiles());
			reInfo.setCommitCount(javaParserVisitor.getCommitCounter());
			reInfo.setBranchesCount(extractInfo.getNumberOfBranches());
			reInfo.setContributorsCount(extractInfo.getNumberOfDevs());
			reInfo.setForksCount(extractInfo.getNumberOfForks());
			reInfo.setHtmlUrl(urlProject);
			reInfo.setId(extractInfo.getID());
			reInfo.setReleaseCount(extractInfo.getNumberOfReleases());
			reInfo.setStarsCount(extractInfo.getNumberOfStars());
			reInfo.setWatchersCount(extractInfo.getNumberOfWatchers());

			try {
				csvBuilder.write(reInfo.getCsvLine());
			} catch (Exception e) {
				System.out.println("error to write results of: " + urlProject);
				continue;
			}

			try {
				FileUtils.deleteDirectory(new File(Folders.REPOS_FOLDER + File.separator + reponame));
			} catch (IOException e) {
				System.out.println("error to delete directory of: " + urlProject);
			}
		}

		try {
			csvBuilder.close();
		} catch (Exception e) {
			System.out.println("error to close csv file");
		}

		// TODO remember to cleaner github token
	}

	private static List<String> getUrls() {
		String jsonPath = PropertiesManager.getProperty("url.list.json");
		List<String> urls = new ArrayList<>();

		JSONParser parser = new JSONParser();
		JSONArray jsonArray = null;

		try {
			jsonArray = (JSONArray) parser.parse(new FileReader(jsonPath));
		} catch (Exception e) {
			System.out.println("error to get json file");
			e.printStackTrace();
			return null;
		}

		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			String url = (String) jsonObject.get("url");
			urls.add(url);
		}

		return urls;
	}
}
