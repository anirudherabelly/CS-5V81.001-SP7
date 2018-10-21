/**
 * @author : 
 * Anirudh Erabelly
 * Ketki Mahajan
 */
package axe170009;

import java.util.HashSet;
import java.util.Random;

public class Hashing<K extends Comparable<? super K>> {
	
	//HashNode class
    private class HashNode<T> {
        T key;
        boolean isDeleted;

        //constructor of HashNode class
        public HashNode(T key) {
            this.key = key;
            this.isDeleted = false;
        }
    }

    int size;
    int capacity;
    float loadFactor = 0.5f;
    HashNode<K>[] table;

    /*This function ensures that hashCodes that differ only by
        constant multiples at each bit position have a bounded
        number of collisions (approximately 8 at default load factor).*/
    private static int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

	public Hashing() {
		capacity = 32;
		size = 0;
        loadFactor = 0.5f;
        table = new HashNode[capacity];
		for(int i = 0; i < capacity; i++) {
			table[i] = null;
		}
	}

    private static int indexFor(int h, int length) {
        return h & (length-1);
	}

    private int hashHelper(K x) {
        return indexFor(hash(x.hashCode()), this.capacity);
    }

    private int find(K x) {
        int k = 0, ik = 0; //ik is hashValue
        HashNode<K> curNode;
        while(true) {
            ik = (k + hashHelper(x)) % this.capacity;
            curNode = this.table[ik];
            if (curNode == null || (curNode.key.compareTo(x) == 0)) {
                return ik;
            } else if(curNode.isDeleted) {
				break;
			}
			else {
                k++;
            }
        }
        int xSpot = ik;
        while (true) {
        	k++;
        	ik = (k + hashHelper(x)) % this.capacity;
            curNode = this.table[ik];
            
        	if (curNode != null && curNode.key.compareTo(x) == 0) {
                return ik;
            }
            if(curNode == null) {
				return xSpot;
			}
		}
	}
	
	public boolean contains(K x) {
		int location = find(x);
		HashNode<K> node = this.table[location];
        return (node != null) && (node.key.compareTo(x) == 0) && !node.isDeleted;
    }

    public K remove(K x) {
        int location = find(x);
        if (this.table[location] != null && this.table[location].key.compareTo(x) == 0 && !this.table[location].isDeleted) {
            HashNode<K> result = this.table[location];
            this.table[location].isDeleted = true;
            this.size--;
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
			if(node == null || node.isDeleted) {
				this.table[location] = new HashNode<K>(x);
                size++;

                if ((float) size / capacity > loadFactor) {
                    rebuildAndRehash();
                }
                return true;
            }
			else if(displacement(this.table[location].key, location) >= disp) {
                disp += 1;
                location = (location + 1) % this.capacity;
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
	
	// Calculate displacement of x from its ideal location of h( x )
	private int displacement(K x, int location) {
        int hashValue = hashHelper(x);
        return (location >= hashValue) ? (location - hashValue) : (this.capacity + location - hashValue);
    }

    private void rebuildAndRehash() {
        HashNode[] temp = new HashNode[this.size];
        this.size = 0;
        int j = 0;
        for (HashNode e : table) {
            if (e != null && !e.isDeleted && j < temp.length) {
                temp[j++] = e;
            }
        }
        this.capacity *= 2;
        this.table = new HashNode[this.capacity];
        for (int i = 0; i < temp.length; i++) {
            RobinHoodAdd((K) temp[i].key);
        }
    }
	
    public static void main(String[] args) {
		// driver methods
		Hashing<Integer> table = new Hashing<Integer>();
		System.out.println(table.RobinHoodAdd(1000));
		System.out.println(table.RobinHoodAdd(1001));
		System.out.println(table.contains(1002));
		System.out.println(table.size);
		System.out.println(table.remove(1000));
		System.out.println(table.contains(1000));
		System.out.println(table.remove(1000));
		System.out.println(table.size);
        System.out.println(table.RobinHoodAdd(1000));
        System.out.println(table.RobinHoodAdd(1001));
        System.out.println(table.RobinHoodAdd(10));
        System.out.println(table.RobinHoodAdd(160));
        System.out.println(table.size);
		
		//to compare performance of implemented hashing with hashset.
		Random random = new Random();
		int length = 10;
		Integer[] arr = new Integer[length];
		for(int i = 0; i < length; i++) {
			arr[i] = random.nextInt(Integer.MAX_VALUE);
			System.out.println(arr[i]);
		}
		int distinctUsingHashing = distinctElements(arr, length);
		int distinctUsingHashSet = distinctElementsUsingHashSet(arr, length);
		System.out.println(distinctUsingHashing+ " "+distinctUsingHashSet);
    }
    
	public static<T extends Comparable<? super T>> int distinctElements(T[ ] arr, int length) {
		Hashing<T> table = new Hashing<T>();
		int count = 0;
		boolean isAdded;
		for(int i = 0; i < length; i++) {
			isAdded = table.RobinHoodAdd(arr[i]);
			if(isAdded)count++;
		}
		return count;
	}
	
	public static<T> int distinctElementsUsingHashSet(T[ ] arr, int length) {
		HashSet<T> set = new HashSet<T>();
		int count = 0;
		boolean isAdded;
		for(int i = 0; i < length; i++) {
			isAdded = set.add(arr[i]);
			if(isAdded)count++;
		}
		return count;
	}

}
