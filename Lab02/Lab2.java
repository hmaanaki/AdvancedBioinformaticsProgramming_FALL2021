package com.something;
import java.util.Random;
import java.util.Scanner;

public class Lab2 {

	public static String[] SHORT_NAMES = 
		{ "A","R", "N", "D", "C", "Q", "E", 
		"G",  "H", "I", "L", "K", "M", "F", 
		"P", "S", "T", "W", "Y", "V" };

	public static String[] FULL_NAMES = 
		{
		"alanine","arginine", "asparagine", 
		"aspartic acid", "cysteine",
		"glutamine",  "glutamic acid",
		"glycine" ,"histidine","isoleucine",
		"leucine",  "lysine", "methionine", 
		"phenylalanine", "proline", 
		"serine","threonine","tryptophan", 
		"tyrosine", "valine"};

	public static void main(String[] args) {
		
		int correctAnswers = 0;
		long startTime = System.currentTimeMillis();
		long ellapsedTime = 0;
		
		Random random = new Random();
		
		while(ellapsedTime < 30000) {
			
			System.out.println("You have " + (30 - ellapsedTime/1000) + " seconds remaining!");
			
			int n = random.nextInt(20);
			System.out.println("What is the single letter code for " + FULL_NAMES[n] + "?");
			
			String userInput = System.console().readLine().toUpperCase();
			
			if(userInput.equals(SHORT_NAMES[n])) {
				System.out.println("Correct!");
				correctAnswers += 1;
			}
			else if(userInput.equals("QUIT")){
				System.out.println("How are you going to improve if you quit now?");
			}
			else {
				System.out.println("That's incorrect! The correct answer is: " + SHORT_NAMES[n]);
				System.out.println("The number of questions answered correctly is: " + correctAnswers);
				break;
			}
			
			long currentTime = System.currentTimeMillis();
			ellapsedTime = currentTime - startTime;
		}
		if(ellapsedTime>= 30000) {
			System.out.println("Your 30 seconds have passed!");
		}
	}
}
