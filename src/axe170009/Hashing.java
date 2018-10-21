/**
 * @author : 
 * Anirudh Erabelly
 * Ketki Mahajan
 */
package axe170009;

public class Hashing<K extends Comparable<? super K>> {
    int size;
    int capacity;
    int loadFactor;
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
		loadFactor = 0;
		table = new HashNode[capacity];
		for(int i = 0; i < capacity; i++) {
			table[i] = null;
		}
	}

    public static void main(String[] args) {
        // driver methods
        Hashing<Integer> tableArr = new Hashing<>();
        System.out.println(tableArr.RobinHoodAdd(1000));
        System.out.println(tableArr.RobinHoodAdd(1001));
        System.out.println(tableArr.RobinHoodAdd(10));
        System.out.println(tableArr.RobinHoodAdd(160));

        System.out.println(tableArr.contains(1001));
        System.out.println(tableArr.remove(1000));
        System.out.println(tableArr.contains(1000));
        System.out.println(tableArr.size);


    }

    private static int indexFor(int h, int length) {
        return h & (length-1);
	}

    public int hashHelper(K x) {
        return indexFor(hash(x.hashCode()), this.capacity);
    }

    private int find(K x) {
        int k = 0, ik = 0; //ik is hashValue
        HashNode<K> curNode;
        while(true) {
            ik = (k + hashHelper(x)) % this.capacity;
            curNode = this.table[ik];

            if (curNode == null || (curNode.key.compareTo(x) == 0) || curNode.isFree) {
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
            if (curNode.key.compareTo(x) == 0) {
                return ik;
            }
            if(curNode.isFree) {
				return xSpot;
			}
		}
	}
	
	public boolean contains(K x) {
		int location = find(x);
		HashNode<K> node = this.table[location];
        return node != null && (node.key.compareTo(x) == 0);
    }

    public K remove(K x) {
        int location = find(x);
        if (this.table[location].key.compareTo(x) == 0) {
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
			if(node == null || node.isFree || node.isDeleted) {
				this.table[location] = new HashNode<K>(x);
                size++;
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

    //HashNode class
    private class HashNode<T> {
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

    private void rebuildAndRehash() {
        HashNode[] temp = new HashNode[this.size];
        this.size = 0;
        int j = 0;
        for (HashNode e : table) {
            if (e != null && !e.isDeleted && !e.isFree && j < temp.length) {
                temp[j++] = e;
            }
        }
        this.capacity *= 2;
        this.table = new HashNode[this.capacity];
        for (int i = 0; i < temp.length; i++) {
            RobinHoodAdd((K) temp[i].key);
        }
    }

    public static<T> int distinctElements(T[ ] arr) {
		return 0;	
	}

}
