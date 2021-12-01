package lab6;

import java.util.concurrent.Semaphore;

public class SomethingSlow implements Runnable{
	
	static int num_threads;
	private final Semaphore semaphore;
	
	public SomethingSlow(Semaphore semaphore) {
		this.semaphore = semaphore;
	}
	
	static int findPrimeNumbers(int user_value) {
		return user_value;
	}
	
	public void run() {
		try {
			findPrimeNumbers(10);
		}
		catch(Exception e) {
			
		}
		finally {
			semaphore.release();
		}
	}
	
	public static void main(String[] args) throws Exception{
		
		Semaphore semaphore = new Semaphore(num_threads);
		for(int i = 0; i < num_threads; i++) {
			semaphore.acquire();
			SomethingSlow worker = new SomethingSlow(semaphore);
			new Thread(worker).start();
		}
		
		for(int i=0; i<num_threads; i++) {
			semaphore.acquire();
		}
	}
	
	
}
