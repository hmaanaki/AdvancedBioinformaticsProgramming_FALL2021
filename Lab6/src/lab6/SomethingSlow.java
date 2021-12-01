package lab6;

import java.util.concurrent.Semaphore;

public class SomethingSlow implements Runnable{
	
	static int num_threads;
	private static long value_to_factor = 0;
	private final Semaphore semaphore;
	
	public SomethingSlow(Semaphore semaphore, long value_to_factor) {
		this.value_to_factor = value_to_factor;
		this.semaphore = semaphore;
	}
	
	static void find_prime_numbers(long value_to_factor) {
		
		long half = value_to_factor/2 +1;	
		for(long i=2; i<half; i++) {
			if(value_to_factor % i == 0) {
				System.out.println(i);
			}
		}
	}
	
	public void run() {
		try {
			find_prime_numbers(value_to_factor);
		}
		catch(Exception e) {
			System.out.println("Oh no.");
		}
		finally {
			semaphore.release();
		}
	}
	
	public static void call_main(int num_threads, long value_to_factor) throws Exception {
		SomethingSlow.value_to_factor = value_to_factor;
		SomethingSlow.num_threads = num_threads;
		main(null);
	}
	
	public static void main(String[] Args) throws Exception{
		
		long start_time = System.currentTimeMillis();
		Semaphore semaphore = new Semaphore(num_threads);
		for(int i = 0; i < num_threads; i++) {
			semaphore.acquire();
			SomethingSlow worker = new SomethingSlow(semaphore, value_to_factor);
			new Thread(worker).start();
		}
		
		for(int i=0; i<num_threads; i++) {
			semaphore.acquire();
		}
		System.out.println("Ellapsed Time: " + (System.currentTimeMillis()- start_time)/100f);
	}
	
	
}
