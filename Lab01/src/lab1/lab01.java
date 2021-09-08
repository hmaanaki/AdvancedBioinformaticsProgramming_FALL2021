package lab1;

import java.util.Random;


public class lab01 {

	public static void main(String[] args) {
		
		Random random = new Random();
		String sequence = "";
		String base = "";
		
		for(int i = 0; i<3000; i++) {
			int n = random.nextInt(4);
			
			if(n == 0){
				base = "A";
			}
			else if(n==1) {
				base = "T";
			}
			else if(n==2) {
				base = "G";
			}
			else {
				base = "C";
			}
			sequence = sequence + base;
		}
		
		int tripleACounter = 0;
		for(int j = 0; j < sequence.length();j+=3){
			
			String codon = sequence.substring(j,j+3);
			
			if(codon.equals("AAA")){
				 tripleACounter = tripleACounter + 1;
			}
		}
		
		int numCodons = sequence.length()/3;
		float percentTripleA = ((float)tripleACounter/numCodons)*100;
		float theoreticalTripleA = (float)1/64 * 100;
		
		System.out.println("The percentage of the codon AAA within the sequence is: " + percentTripleA + "%");
		System.out.println("The percentage of the codon AAA appearing within 1000 codons is: " + theoreticalTripleA + "%");
		float tripleADiff = Math.abs(percentTripleA - theoreticalTripleA);
		
		if(tripleADiff < 0.5) {
			System.out.println("The percentage of the codon AAA appearing is close to the theoretical value!");
		
		}	
	}
}
