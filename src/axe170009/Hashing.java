/**
 * @author : 
 * Anirudh Erabelly
 * Ketki Mahajan
 * 
 * Java implementation of Robinhood hashing(Open Addressing technique). 
 */

package axe170009;

import java.util.HashSet;
import java.util.Random;

public class Hashing < K extends Comparable < ? super K >> {
	
	private static final float threshold = 0.5f;
    
	//HashNode class
    private class HashNode < T > {
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
    HashNode < K > [] table;
    
    //constructor for hashing class
    @SuppressWarnings("unchecked")
	public Hashing() {
        capacity = 2048;
        size = 0;
        table = new HashNode[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
    }

    /*This function ensures that hashCodes that differ only by
    constant multiples at each bit position have a bounded
    number of collisions (approximately 8 at default load factor).*/
    private static int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
    
    //returns index in the length range.
    private static int indexFor(int h, int length) {
        return h & (length - 1);
    }
    
    //util method to return index to store an entry in hash table
    private int hashHelper(K x) {
        return indexFor(hash(x.hashCode()), this.capacity);
    }
    
    //find the position of entry in hash table.
    private int find(K x) {
        int k = 0, ik = 0; //ik is hashValue
        HashNode < K > curNode;
        while (true) {
            ik = (k + hashHelper(x)) % this.capacity;
            curNode = this.table[ik];
            if (curNode == null || (curNode.key.compareTo(x) == 0)) {
                return ik;
            } else if (curNode.isDeleted) {
                break;
            } else {
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
            if (curNode == null) {
                return xSpot;
            }
        }
    }
    
    //check whether entry is present in hash table or not
    public boolean contains(K x) {
        int location = find(x);
        HashNode < K > node = this.table[location];
        return (node != null) && (node.key.compareTo(x) == 0) && !node.isDeleted;
    }
    
    //remove an entry from hash table and return it, if present else null.
    public K remove(K x) {
        int location = find(x);
        if (this.table[location] != null && this.table[location].key.compareTo(x) == 0 && !this.table[location].isDeleted) {
            this.table[location].isDeleted = true;
            this.size--;
            return this.table[location].key;
        }
        return null;
    }
    
    //add into hashtable using robinhood hashing
    public boolean RobinHoodAdd(K x) {
        if (contains(x)) return false;
        int location = find(x);
        int disp = 0;
        while (true) {
            HashNode < K > node = this.table[location];
            if (node == null || node.isDeleted) {
                this.table[location] = new HashNode < K > (x);
                size++;
                
                if (getLoadfactor() > threshold) {
                    rebuildAndRehash();
                }
                return true;
            } else if (displacement(this.table[location].key, location) >= disp) {
                disp += 1;
                location = (location + 1) % this.capacity;
            } else {
                K temp = x;
                x = this.table[location].key;
                this.table[location].key = temp;
                location = (location + 1) % this.capacity;
                disp = displacement(x, location);
            }
        }
    }

    //calculates the load factor of a given table.
    private float getLoadfactor() {
        return ((float) this.size) / this.capacity;
    }

    // Calculate displacement of x from its ideal location of h( x )
    private int displacement(K x, int location) {
        int hashValue = hashHelper(x);
        return (location >= hashValue) ? (location - hashValue) : (this.capacity + location - hashValue);
    }
    
    //rebuilds and rehashes whenever load-factor > 0.5
    @SuppressWarnings("unchecked")
	private void rebuildAndRehash() {
        HashNode<K>[] temp = new HashNode[this.size];
        this.size = 0;
        int j = 0;
        for (HashNode<K> e: this.table) {
            if (e != null && !e.isDeleted && j < temp.length) {
                temp[j++] = e;
            }
        }
        this.capacity *= 2;
        this.table = new HashNode[this.capacity];
        for (int i = 0; i < temp.length; i++) {
            RobinHoodAdd(temp[i].key);
        }
    }
    
    //main driver method
    public static void main(String[] args) {
        Hashing < Integer > table = new Hashing < Integer > ();
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
        int length = 1000000; //1 million entries.
        Integer[] arr = new Integer[length];
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(Integer.MAX_VALUE);
        }
        Timer timer = new Timer();
        int distinctUsingHashing = distinctElements(arr, length);
        timer.end();
        System.out.println("No. of distinct elements using implemented hashing : "+distinctUsingHashing);
        System.out.println(timer.toString());
        
        timer.start();
        int distinctUsingHashSet = distinctElementsUsingHashSet(arr, length);
        timer.end();
        System.out.println("No. of distinct elements using hashset : "+distinctUsingHashSet);
        System.out.println(timer.toString());
        
    }
    
    //distinct elements in ana array using robinhood hashing
    public static < T extends Comparable <? super T >> int distinctElements(T[] arr, int length) {
        Hashing < T > table = new Hashing < T > ();
        int count = 0;
        boolean isAdded;
        for (int i = 0; i < length; i++) {
            isAdded = table.RobinHoodAdd(arr[i]);
            if (isAdded) count++;
        }
        return count;
    }
    
  //distinct elements in ana array using Java's HashSet
    public static < T > int distinctElementsUsingHashSet(T[] arr, int length) {
        HashSet < T > set = new HashSet < T > ();
        int count = 0;
        boolean isAdded;
        for (int i = 0; i < length; i++) {
            isAdded = set.add(arr[i]);
            if (isAdded) count++;
        }
        return count;
    }
}