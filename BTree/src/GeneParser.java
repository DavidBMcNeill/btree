
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
	 * a = 00; t = 11; c = 01; g = 10
	 * @param dnaString
	 * @return dna string formatted as a long binary number. 
	 */
	private long toBinary(String dnaString) {
		long binary = 0;
		char[] letters = dnaString.toCharArray();
		StringBuilder binaryBuild = new StringBuilder();
		
		for (char letter : letters) {
			switch(letter) {
			case 'a':
				binaryBuild.append("00");
			case 't':
				binaryBuild.append("11");
			case 'c':
				binaryBuild.append("01");
			case 'g':
				binaryBuild.append("10");
			default:
				System.err.println("Invalid dna char input: " + letter);
			}
		}
		binary = Long.parseLong(binaryBuild.toString());
		return binary;
	}
	
	/**
	 * writes a text file named 'dump that has following line format:
	 * <frequency> <DNA string>
	 */
	public ArrayList<BTreeObject> parse() {

		String genes = "atcg";
		String firstScan = "";
		String sequence = "";

		StringBuilder geneInfo = new StringBuilder();
		ArrayList<BTreeObject> treeObjects = new ArrayList<BTreeObject>();

		try {
			Scanner geneScan = new Scanner(new File(file));
			HashMap<String, Integer> geneCount = new HashMap<String, Integer>();

// 			PrintWriter dump = new PrintWriter(new File("dump"));

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
				long key = toBinary(dnaString);
				int duplicateCount = entry.getValue();
				System.out.println("<" + duplicateCount + "> " + "<" + key + ">");
				
				treeObjects.add(treeObject);
				// no longer printing to dump b/c needs to be IN-ORDER TRAVERSAL
// 				dump.println("<" + frequency + "> " + "<" + dnaString + ">");
			}

			dump.close();
			geneScan.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return treeObjects;
	}
}
