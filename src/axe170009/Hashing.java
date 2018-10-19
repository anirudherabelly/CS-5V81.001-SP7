/**
 * @author : 
 * Anirudh Erabelly
 * Ketki Mahajan
 */
package axe170009;

import java.util.Arrays;

public class Hashing<K> {
	
	//HashNode class
	private class HashNode<T>{
		T key;
		boolean isDeleted;
		boolean isFree = true;
		
		//constructor of HashNode class
		public HashNode(T key) {
			this.key = key;
			this.isDeleted = false;
			this.isFree = false;
		}
	}
	
	HashNode<K>[] table;
	int size;
	int capacity;
	int loadFactor;
	
	public Hashing() {
		capacity = 32;
		size = 0;
		loadFactor = 0;
		table = new HashNode[capacity];
		for(int i = 0; i < capacity; i++) {
			table[i] = null;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
		result = prime * result + size;
		result = prime * result + Arrays.hashCode(table);
		return result;
	}

	private static int hash(int h) {
		/*This function ensures that hashCodes that differ only by
		constant multiples at each bit position have a bounded
		number of collisions (approximately 8 at default load factor).*/
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}
	
	private static int indexFor(int h, int length) {
		return h & (length-1);
	}
	
	private int find(K x) {
		int spot = 0, hashValue = 0;
		HashNode<K> curNode;
		while(true) {
			hashValue = indexFor(hash(x.hashCode()), this.capacity);
			curNode = this.table[hashValue];
			
			if(curNode == null || curNode.key.equals(x) || curNode.isFree) {
				return hashValue;
			}
			else if(curNode.isDeleted) {
				break;
			}
			else {
				spot++;
			}
		}
		int xSpot = hashValue;
		while(true) {
			spot++;
			if(curNode.key.equals(x)) {
				return hashValue;
			}
			if(curNode.isFree) {
				return xSpot;
			}
		}
	}
	
	public boolean contains(K x) {
		int location = find(x);
		HashNode<K> node = this.table[location];
		if(node != null && node.key.equals(x))return true;
		return false;
	}
	
	public K remove(K x) {
		int location = find(x);
		if(this.table[location].key.equals(x)) {
			HashNode<K> result = this.table[location];
			this.table[location].isDeleted = true;
			return result.key;
		}
		return null;
	}
	
	public boolean RobinHoodAdd(K x) {
		if(contains(x))return false;
		int location = find(x);
		int disp = 0;
		while(true) {
			HashNode<K> node = this.table[location];
			if(node == null || node.isFree || node.isDeleted) {
				this.table[location] = new HashNode<K>(x);
				return true;
			}
			else if(displacement(this.table[location].key, location) >= disp) {
				disp+=1;
				location = (location+1)%this.capacity;
			}
			else {
				K temp = x;
				x = this.table[location].key;
				this.table[location].key = temp;
				location = (location+1)%this.capacity;
				disp = displacement(x, location);
			}
		}
	}
	
	public boolean HopScotchAdd(K x) {
		return false;
	}
	
	// Calculate displacement of x from its ideal location of h( x )
	private int displacement(K x, int location) {
		int hashValue = indexFor(hash(x.hashCode()), this.capacity);
		return (location >= hashValue) ? (location - hashValue) : (this.capacity + location - hashValue);
	}
	
	public static void main(String[] args) {
		// driver methods
		Hashing<Integer> table = new Hashing<Integer>();
		System.out.println(table.RobinHoodAdd(1000));
		System.out.println(table.contains(1001));
	}
	
	public static<T> int distinctElements(T[ ] arr) {
		return 0;	
	}

}
