package io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PhoneList01 {

	public static void main(String[] args) {
		BufferedReader br = null;
		try {
			//기반스트림
			//FileInputStream fs = new FileInputStream("phone.txt"); // 밑에 fs 대신 통째로 넣어줌
			
			//보조스트림1(bytes->char)
			//InputStreamReader isr = new InputStreamReader(new FileInputStream("phone.txt"), "UTF-8"); // 밑에 isr 대신 통째로 넣어줌
			br = new BufferedReader(new InputStreamReader(new FileInputStream("phone.txt"), "UTF-8"));
			
			String line = null;
			while((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "\t ");
				
				
				int index = 0; 
				
				while(st.hasMoreElements()) {
					String token = st.nextToken();
					System.out.print(token);
					if(index == 0) {//이름
						System.out.print(":");
					} else if(index == 1) { //전화번호
						System.out.print("-");
					} else if(index == 2) { //전화번호
						System.out.print("-");
					} 
					
					index++;
				}
				System.out.print("\n");
				//System.out.println(line);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
