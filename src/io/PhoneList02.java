package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PhoneList02 {

	public static void main(String[] args) {
		//inputStream 넣어줘도 o
		//file객체 넣어줘도 o
		//File file = new File("phone.txt");
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("phone.txt"));
			
			while(scanner.hasNextLine()) {
				String name = scanner.next();
				String phone01 = scanner.next();
				String phone02 = scanner.next();
				String phone03 = scanner.next();
				
				System.out.println(
					name + ":" +
					phone01 + "-" +
					phone02 + "-" +
					phone03
				);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}

	}

}
