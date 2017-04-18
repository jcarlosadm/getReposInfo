package getreposinfo.repodriller;

import java.util.List;
import java.util.Vector;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

public class JavaParserVisitor implements CommitVisitor {

	private int commitCounter = 0;
	private int javaLineCounter = 0;
	
	private List<String> files = new Vector<>();

	@Override
	public String name() {
		return "repo basic info";
	}

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism pMechanism) {
		
		++this.commitCounter;
		
		for (Modification modification : commit.getModifications()) {

			if (modification.wasDeleted() || !modification.getFileName().endsWith(".java")) {
				continue;
			}
			
			int numberOfLines = this.getLines(modification.getSourceCode());
			if (numberOfLines > this.javaLineCounter) {
				this.javaLineCounter = numberOfLines;
			}
			
			if (!this.files.contains(modification.getFileName())) {
				this.files.add(modification.getFileName());
			}
			
		}
	}

	private int getLines(String sourceCode) {
		String[] lines = sourceCode.split("\r\n|\r|\n");
		return lines.length;
	}

	public void resetCounters() {
		this.commitCounter = 0;
		this.javaLineCounter = 0;
		this.files.clear();
	}
	
	public int getNumberOfFiles() {
		return this.files.size();
	}

	public int getCommitCounter() {
		return commitCounter;
	}

	public int getJavaLineCounter() {
		return javaLineCounter;
	}

}
