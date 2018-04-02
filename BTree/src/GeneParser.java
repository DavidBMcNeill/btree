
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * // search file determined by <gbk file> entered as cmd-line arg for //
 * GeneBankCreateBTree
 * 
 * // writes a text file named "dump" // following line format: // <frequency>
 * <DNA string>
 * 
 * // save each n-gram to a hashtable -- count duplicates
 * 
 * @author DavidMcNeill1
 *
 */
public class GeneParser {

	// var for gbk file, read by GeneBankCreateBTree cmd-line arg
	String file;

	// variable for sequence length; read by GeneBankCreateBTree cmd-line arg
	int ngram;

	public GeneParser(String file, int ngram) {
		this.file = file;
		this.ngram = ngram;
	}

	/**
	 * writes a text file named 'dump that has following line format:
	 * <frequency> <DNA string>
	 */
	public void parse() {

		String genes = "atcg";
		String firstScan = "";
		String sequence = "";

		StringBuilder geneInfo = new StringBuilder();

		try {
			Scanner geneScan = new Scanner(new File(file));
			HashMap<String, Integer> geneCount = new HashMap<String, Integer>();

			PrintWriter dump = new PrintWriter(new File("dump"));

			while (geneScan.hasNextLine()) {
				firstScan = geneScan.nextLine();
				if (firstScan.contains("ORIGIN")) {
					firstScan = geneScan.nextLine();
					while (!firstScan.contains("//")) {
						// eliminate all line #s
						firstScan = firstScan.replaceAll("[^A-z]+", "");
						geneInfo.append(firstScan);
						firstScan = geneScan.nextLine().toLowerCase();
					}
				}
			}

			for (int i = 0; i < geneInfo.length(); i++) {

				// split each line of genetic info into sequences of
				// ngram length, moving 1 char at a time
				if (geneInfo.length() >= i + ngram) {
					sequence = geneInfo.substring(i, (i + ngram));

					// checks that sequence only contains 'a'
					// 't'
					// 'c' or 'g'
					if (sequence.indexOf('n') == -1) {

						// if substring has already been found,
						// increment frequency
						if (geneCount.containsKey(sequence)) {
							int frequency = geneCount.get(sequence);
							geneCount.replace(sequence, frequency, ++frequency);
						} else {
							// if not, add to HashMap and give
							// frequency of 1
							geneCount.put(sequence, 1);
						}
					}
				}
			}
			System.out.println(geneCount);

			for (Map.Entry<String, Integer> entry : geneCount.entrySet()) {
				String dnaString = entry.getKey().toString();
				int frequency = entry.getValue();
				System.out.println("<" + frequency + "> " + "<" + dnaString + ">");
				dump.println("<" + frequency + "> " + "<" + dnaString + ">");
			}

			dump.close();
			geneScan.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
