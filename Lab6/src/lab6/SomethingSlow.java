package lab6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SomethingSlow implements Runnable{
	
	static int num_threads;
	private static int value_to_factor = 0;
	private final Semaphore semaphore;
	private static int min_val = 0;
	private static int max_val = 0;
	private volatile static boolean completed = true;
	
	private static final List<Integer> list_of_primes = Collections.synchronizedList(new ArrayList<Integer>());
	
	public SomethingSlow(Semaphore semaphore, int min_val, int max_val) {
		SomethingSlow.min_val = min_val;
		SomethingSlow.max_val = max_val;
		this.semaphore = semaphore;
	}
	
	static void find_prime_numbers(int min_val, int max_val) {
		for(int i=min_val; i<max_val; i++) {
			Boolean prime = true;
			for(int j=2; j < i; j++) {
				if(i % j == 0) {
					prime = false;
				}
			}
			if(prime & i != 0 & i != 1) {
				synchronized (list_of_primes) {
					list_of_primes.add(i);
				}
			}
		}
	}
	
	public static int number_primes_found() {
		return list_of_primes.size();
	}
	
	public static boolean calculation_completed(){
		return completed;
	}
	
	public static List<Integer> get_list_of_primes(){
		return list_of_primes;
	}
	
	public void run() {
		try {
			find_prime_numbers(min_val, max_val);
		}
		catch(Exception e) {
			System.out.println("Oh no.");
		}
		finally {
			semaphore.release();
		}
	}
	
	public static void call_main(int num_threads, int value_to_factor) throws Exception {
		SomethingSlow.value_to_factor = value_to_factor;
		SomethingSlow.num_threads = num_threads;
		main(null);
	}
	
	public static void main(String[] Args) throws Exception{
		
		long start_time = System.currentTimeMillis();
		int interval_spacing = value_to_factor / num_threads;
		
		Semaphore semaphore = new Semaphore(num_threads);
		
		ConcurrentMap<Integer, List<Integer>> intervals = new ConcurrentHashMap<Integer, List<Integer>>();
		for(int i =0; i < num_threads; i++) {
			min_val = max_val;
			max_val = min_val + interval_spacing;
			List<Integer> interval = new ArrayList<Integer>();
			interval.add(min_val);
			interval.add(max_val);
			intervals.put(i+1, interval );
		}
		
		for(int i = 0; i < num_threads; i++) {
			semaphore.acquire();	
			SomethingSlow worker = new SomethingSlow(semaphore, intervals.get(i+1).get(0), intervals.get(i+1).get(1));
			new Thread(worker).start();
			TimeUnit.SECONDS.sleep(1);
		}
		
		for(int i=0; i<num_threads; i++) {
			semaphore.acquire();
		}		
		completed = false;
		
		System.out.println("The total number of primes found is: " + list_of_primes.size());
		System.out.println("Ellapsed Time: " + (System.currentTimeMillis()- start_time)/1000f);
	}
	
	
}
