package lab1;

import java.util.Random;

public class lab01WithProb {

	public static void main(String args[]) {
		
		Random random = new Random();
		String sequence = "";
		String base = "";
		
		for(int i = 0; i<3000; i++) {
			float n = random.nextFloat();
			
			if(n < 0.12){
				base = "A";
			}
			else if(n >= 0.12 && n < 0.5) {
				base = "C";
			}
			else if(n >= 0.5 && n < 0.89) {
				base = "G";
			}
			else {
				base = "T";
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
		float theoreticalTripleA = (float)Math.pow(0.12,3);
		
		System.out.println("The percentage of the codon AAA within the sequence is: " + percentTripleA + "%");
		System.out.println("The percentage of the codon AAA appearing within 1000 codons is: " + theoreticalTripleA + "%");
		float tripleADiff = Math.abs(percentTripleA - theoreticalTripleA);
		
		if(tripleADiff < 0.2) {
			System.out.println("The percentage of the codon AAA appearing is close to the theoretical value!");
		
		}	
	}
}
