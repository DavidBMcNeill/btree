
/**
 * Contains a long [key value] and an int [duplicate count].
 * @author DavidMcNeill1
 *
 */
public class TreeObject {

	//long is the key value [use to build search tree property [left < mid < right]
	
	//int frequency: increment if duplicate substring encountered
	
	//contains long; int
	
	private int duplicateCount;
	private long key;

	public TreeObject(long key, int duplicateCount) {
		this.key = key;
		this.duplicateCount = duplicateCount;
	}

	/**
	 * compare actual key objects (may have same hashCode() value)
	 * 
	 * @param HashObject
	 *            o, the target in the hashTable.
	 * @return 1 if match; 2 if not; 0 if 'o' is null.
	 */
	public int equals(TreeObject o) {
		int nextStep = -1;
		if (o == null) {
			nextStep = 0;
		} else {
			long compareKey = o.getKey();
			if (key == compareKey) {
				nextStep = 1;
				duplicateCount += 1;
			} else if (key != compareKey) {
				nextStep = 2;
			}
		}
		return nextStep;
	}

	/**
	 * @return long key value
	 */
	public long getKey() {
		return key;
	}
	
	public void duplicateIncrement() {
		duplicateCount++;
	}

	@Override
	public String toString() {
		return "TreeObject [duplicateCount=" + duplicateCount + ", key=" + key + "]";
	}		
}//end TreeObject
