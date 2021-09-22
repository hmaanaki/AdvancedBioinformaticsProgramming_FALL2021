package lab3;
import java.util.Random;

public class lab3 {

	public static String generateRandomSequence(char[] alphabet, float[] weights, int length) throws Exception
	{
		// Initialize variables
		int alphabetLen = alphabet.length;
		int weightLen = weights.length;
		float totalWeight = 0;
		String proteinSequence = "";
		Random random = new Random();
		
		// Condition checks
		if(alphabetLen < 0 || weightLen < 0){
			throw new Exception("Alphabet or weight length is less than zero!");
		}	
		if(alphabetLen != weightLen){
			throw new Exception("Alphabet array length is not equal to weight array length");
		}
		for(int i = 0; i < weightLen; i++) {
			totalWeight += weights[i];
		}
		System.out.println(totalWeight);
		if(totalWeight < 0.99) {
			throw new Exception("Total weight is not within round-off error of 1");
		}
		
		// Generate a sequence of size length using the input characters and weights
		for(int j = 0; j< length; j++) {
			// Multiply the value by the totalWeight so that our range is [0,totalWeight) instead of [0,1)
			float n = random.nextFloat()*totalWeight;
			float sumWeights = 0;
			// Probably not the most efficient but iterate over the weights and generate a moving bound [ sum(weights[k-1]) , sum(weights[k]))
			// Once the bound is generated, check to see if the random float is within that bound
			for(int k = 0; k< weightLen; k++) {
				if(n>=sumWeights && n<weights[k]+ sumWeights) {
					proteinSequence = proteinSequence + alphabet[k];
				}
				sumWeights = sumWeights + weights[k];
			}
		}
		return proteinSequence;	
	}
	
	public static void main(String[] args) throws Exception
	{
		float[] dnaWeights = { .3f, .3f, .2f, .2f };
		char[] dnaChars = { 'A', 'C', 'G', 'T'  };
		
		// a random DNA 30 mer
		System.out.println(generateRandomSequence(dnaChars, dnaWeights,30));
		
		// background rate of residues from https://www.science.org/doi/abs/10.1126/science.286.5438.295
		float proteinBackground[] =
			{0.072658f, 0.024692f, 0.050007f, 0.061087f,
		        0.041774f, 0.071589f, 0.023392f, 0.052691f, 0.063923f,
		        0.089093f, 0.023150f, 0.042931f, 0.052228f, 0.039871f,
		        0.052012f, 0.073087f, 0.055606f, 0.063321f, 0.012720f,
		        0.032955f}; 
			

		char[] proteinResidues = 
				new char[] { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
							 'V', 'W', 'Y' };
		
		// a random protein with 30 residues
			System.out.println(generateRandomSequence(proteinResidues, proteinBackground, 30));
			
		}
	}