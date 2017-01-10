package getreposinfo.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CSVBuilder {
	
	private BufferedWriter bufferedWriter = null;
	
	public CSVBuilder(File csvFile) throws Exception {
		this.bufferedWriter = new BufferedWriter(new FileWriter(csvFile));
	}
	
	public void write(String string) throws Exception {
		this.bufferedWriter.write(string + System.lineSeparator());
	}
	
	public void close() throws Exception {
		this.bufferedWriter.close();
	}
}
