package getreposinfo.repodriller;

import java.io.File;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.scm.GitRepository;

import getreposinfo.util.file.Folders;

public class ExploreRepo implements Study {

	private JavaParserVisitor javaParserVisitor = null;
	private String reponame = "";

	public ExploreRepo(JavaParserVisitor javaParserVisitor, String reponame) {
		this.javaParserVisitor = javaParserVisitor;
		this.reponame = reponame;
	}

	@Override
	public void execute() {
		if (this.javaParserVisitor == null || this.reponame.isEmpty()) {
			System.out.println("java parser visitor or repository name not defined. exiting");
			return;
		}

		this.javaParserVisitor.resetCounters();

		RepositoryMining reMining = new RepositoryMining()
				.in(GitRepository.singleProject(Folders.REPOS_FOLDER + File.separator + this.reponame))
				.through(Commits.all()).process(this.javaParserVisitor);

		this.configureThread(reMining);

		reMining.mine();
	}

	private void configureThread(RepositoryMining reMining) {
		// TODO implement
	}
	
	// TODO remove later
	public static void main(String[] args) {
		String reponame = "Game";
		JavaParserVisitor javaParserVisitor = new JavaParserVisitor();
		ExploreRepo exploreRepo = new ExploreRepo(javaParserVisitor, reponame);
		new RepoDriller().start(exploreRepo);
		System.out.println("commits: "+javaParserVisitor.getCommitCounter());
		System.out.println("lines: "+javaParserVisitor.getJavaLineCounter());
		System.out.println("files: "+javaParserVisitor.getNumberOfFiles());
	}

}
