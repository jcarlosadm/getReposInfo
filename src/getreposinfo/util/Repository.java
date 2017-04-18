package getreposinfo.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import getreposinfo.util.file.Folders;

public abstract class Repository {
	public static boolean cloneRepos() {

		String jsonPath = PropertiesManager.getProperty("url.list.json");

		JSONParser parser = new JSONParser();
		JSONArray jsonArray = null;

		try {
			jsonArray = (JSONArray) parser.parse(new FileReader(jsonPath));
		} catch (Exception e) {
			System.out.println("error to get json file");
			e.printStackTrace();
			return false;
		}

		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			String url = (String) jsonObject.get("url");
			File folder = new File(Folders.REPOS_FOLDER + File.separator + getLastRepoFolderName(url));
			if (!folder.exists() && folder.mkdirs()) {
				try {
					Git.cloneRepository().setURI(url).setDirectory(folder).call();
				} catch (Exception e) {
					try {
						FileUtils.deleteDirectory(folder);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	public static boolean cloneRepo(String urlProject) {

		File repofolder = new File(Folders.REPOS_FOLDER + File.separator + getLastRepoFolderName(urlProject));
		if (!repofolder.exists() && repofolder.mkdirs()) {
			try {
				Git.cloneRepository().setURI(urlProject).setDirectory(repofolder).call();
			} catch (Exception e) {
				try {
					FileUtils.deleteDirectory(repofolder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
				// TODO test if git repository don't exists or private
				return false;
			}
		}

		return true;
	}

	private static String getLastRepoFolderName(String urlProject) {
		String lastRepoFolder;
		if (urlProject.endsWith(".git")) {
			lastRepoFolder = urlProject.substring(urlProject.lastIndexOf("/") + 1, urlProject.lastIndexOf("."));
		} else {
			lastRepoFolder = urlProject.substring(urlProject.lastIndexOf("/") + 1);
		}
		return lastRepoFolder;
	}
}
