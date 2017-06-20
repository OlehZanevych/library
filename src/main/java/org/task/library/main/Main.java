package org.task.library.main;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.task.library.statement.BookStatement;

/**
 * Main class to run application.
 * 
 * @author OlehZanevych
 */
public class Main {
	
	private static final Scanner SCANNER = new Scanner(System.in);
	
	private static boolean IS_ALIVE = true;

	public static void main(String[] args) {
		//Turning off Hibernate logging
		LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
		
		BookStatement bookStatement = BookStatement.getInstance();
		
		System.out.print("P: ");
		System.out.println("I'm Book Library and I'm ready to work. "
				+"I'm waiting for your commands. Please write");
		
		while (IS_ALIVE) {
			System.out.print("U: ");
			String nextLine = nextLine();
			System.out.print("P: ");
			bookStatement.execute(nextLine);
		}
		
		SCANNER.close();
	}
	
	public static void exit() {
		IS_ALIVE = false;
	}
	
	public static String nextLine() {
		return SCANNER.nextLine();
	}

}
