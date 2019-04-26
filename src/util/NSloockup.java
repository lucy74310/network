package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NSloockup {

	public static void main(String[] args) throws UnknownHostException {
		BufferedReader br = null;
		try {
			InputStreamReader isr = new InputStreamReader(System.in , "utf8");
			
			br = new BufferedReader(isr);
			
			//호스트 명
			String hostname = null;
			System.out.println("To close connection, enter \"exit\"");
			while((hostname = br.readLine()) != null) {
				
				if(hostname == null) System.out.println("host is null"); 
				else System.out.println("host:" + hostname);
				
				if(hostname.equals("exit")) break;
				
				//호스트 명 ip[]
				InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
				
				//ip 출력
				if(inetAddresses != null) {
					System.out.println("[addresses]");
					
					for(InetAddress addr : inetAddresses) {
						System.out.println(addr.getHostAddress());
					}
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
