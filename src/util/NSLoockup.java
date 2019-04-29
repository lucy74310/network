package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NSLoockup {

	public static void main(String[] args) throws UnknownHostException {
		BufferedReader br = null;
		try {
			InputStreamReader isr = new InputStreamReader(System.in , "utf8");
			
			br = new BufferedReader(isr);
			
			String hostname = null;
			System.out.println("To close connection, enter \"exit\"");
			System.out.print(">>");
			while((hostname = br.readLine()) != null) {
				//내가짠코드
				//if(hostname.equals("exit")) break;
				//안전한코드 => null exception 일어날일 없다
				if("exit".equals(hostname)) break;
				try {
					//get
					InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
					
					if(inetAddresses != null) {
						System.out.println("[addresses]");
						for(InetAddress addr : inetAddresses) {
							System.out.println(addr.getHostAddress());
						}
					}
				} catch (IOException e) {
					System.out.println("해당 호스트를 찾을 수 없습니다.");
				}
				System.out.print(">>");
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
