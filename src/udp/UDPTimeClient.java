package udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPTimeClient {
	private static final String SERVER_IP = "192.168.1.24";
	
	public static void main(String[] args) {
		Scanner scanner = null;
		DatagramSocket socket = null;
		
		try {
			//1. 스캐너 생성
			scanner = new Scanner(System.in);
			
			//2. 소켓 생성
			socket = new DatagramSocket();
			
			
			while(true) {
				System.out.print(">>");
				String line = scanner.nextLine();
				if("quit".contentEquals(line)) {
					break;
				}
				
				// 4.데이터 쓰기
				byte[] sendData = line.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(
						sendData, 
						sendData.length, 
						new InetSocketAddress(SERVER_IP, UDPTimeServer.PORT));
				socket.send(sendPacket);
				
				//5.데이터 읽기
				DatagramPacket receivePacket = new DatagramPacket(new byte[UDPTimeServer.BUFFER_SIZE], UDPTimeServer.BUFFER_SIZE);
				socket.receive(receivePacket);
				String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
				
				// 6. 콘솔출력
				System.out.println("<<" + message);
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("getBytes('UTF-8') ERROR" );
		} catch (IOException e) {
			System.out.println("socket.send(); ERROR");
		}
	}

}
