package lab4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FastaSequence {
	private final String sequence;
	private final String header;
	
	public FastaSequence(String sequence, String header) {
		this.sequence = sequence;
		this.header = header;
	}
	
	public String getHeader() {
		return header;
	}
	
	public String getSequence() {
		return sequence;
	}
	
	// Returns the GC ratio in percent
	public float getGCRatio() {
		float gcTotal = 0;
		for(int i = 0; i < sequence.length(); i++) {
			if(sequence.charAt(i) == 'G' || sequence.charAt(i) == 'C') {
				gcTotal++;
			}
		}
		return (gcTotal/sequence.length())*100;
	}
	
	// Returns a map with the amount of A, C, G, and T for a FataSequence Object
	public Map<Character, Integer> countNucleotides() {
		Map<Character, Integer> nucleotideCounter = new TreeMap<Character, Integer>();
		nucleotideCounter.put('A',0);
		nucleotideCounter.put('C',0);
		nucleotideCounter.put('G',0);
		nucleotideCounter.put('T',0);
		
		for(int i = 0; i < sequence.length(); i++) {
			int count = nucleotideCounter.get(sequence.charAt(i));
			count++;
			nucleotideCounter.put(sequence.charAt(i), count);
		}
		return nucleotideCounter;
	}
	
	// Writes a summary table with all the FastaSequence objects
	public static void writeTableSummary(List<FastaSequence> list, File outputFile) throws Exception {
		BufferedWriter rowWriter = new BufferedWriter(new FileWriter(outputFile));
		rowWriter.write("SequenceID\tnumA\tnumC\tnumG\tnumT\tsequence\n");
		
		for(FastaSequence seq : list) {
			rowWriter.write(seq.getHeader() + "\t"+ seq.countNucleotides().get('A') + "\t"
			+ seq.countNucleotides().get('C') +"\t"+ seq.countNucleotides().get('G') 
			+ "\t"+ seq.countNucleotides().get('T') + "\t" + seq.getSequence() + "\n");
			
		}
		rowWriter.close();
		
	}
	
	// Reads a Fasta file and returns a list of FastaSequence objects
	public static List<FastaSequence> readFastaFile(String f) throws IOException{
		File file = new File(f);
		BufferedReader readFile = new BufferedReader(new FileReader(file));
		
		List<FastaSequence> listOfSequences = new ArrayList<FastaSequence>();
		StringBuffer sequenceBuff = new StringBuffer();
		
		String headerLine = "";
		String line;
		while((line = readFile.readLine()) != null) {
			if(line.startsWith(">")) {
				if(headerLine.isEmpty() != true) {
					listOfSequences.add(new FastaSequence(sequenceBuff.toString(),headerLine));
				}			
				headerLine = line;
				sequenceBuff.setLength(0);
			}
			else {
				sequenceBuff.append(line);
			}
		}
		listOfSequences.add(new FastaSequence(sequenceBuff.toString(),headerLine));
		readFile.close();
		
		return(listOfSequences);	
	}
	
	public static void main(String[] args) throws Exception{
		List<FastaSequence> fastaList = FastaSequence.readFastaFile("C:\\Users\\hmaan\\Desktop\\AdvancedProgramming\\MDomestica_first100.FA");
		
		for(FastaSequence fs : fastaList) {
			System.out.println(fs.getHeader());
			System.out.println(fs.getSequence());
			System.out.println(fs.getGCRatio());
		}
		
		File myFile = new File("C:\\Users\\hmaan\\Desktop\\AdvancedProgramming\\output.txt");
		
		writeTableSummary(fastaList,myFile);
	}

	
}
