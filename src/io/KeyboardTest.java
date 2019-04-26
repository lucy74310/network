package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

//Ű����� ����
public class KeyboardTest {
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = null;
		try {
			//기반스트림(표준입력, 키보드, System.in)
		
			//보조스트림1
			// byte|byte|byte -> char
			InputStreamReader isr = new InputStreamReader(System.in, "utf8");
			
			
			//보조스트림2
			// char1|char1|char1 \n -> "char1char2char3" (String)
			br = new BufferedReader(isr);
			
			
			// read
			String line = null;
			while((line = br.readLine()) != null) {
				if("exit".equals(line)) {
					break;
				}
				System.out.println(">>" + line);
			}
			
			
		} catch (UnsupportedEncodingException e) {
			System.out.println("error : " + e);
			//e.printStackTrace();
		} finally {
			//하나만 닫아도 됨.
			try {
				if(br != null) {
					br.close();
				}
			} catch(Exception e) {
				System.out.println(e);
			}
		}
	}

}
