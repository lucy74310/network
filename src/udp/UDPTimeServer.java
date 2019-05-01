package udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UDPTimeServer {
	public static final int PORT = 8000;
	public static final int BUFFER_SIZE = 1024;
	public static void main(String[] args) {

		DatagramSocket socket = null;
		
		try {
			//1.소켓생성
			socket = new DatagramSocket(PORT);
			consoleLog("packet 수신 대기");
			
			while(true) {
				//2.수신 대기
				DatagramPacket receivePacket = new DatagramPacket( new byte[BUFFER_SIZE], BUFFER_SIZE); 
				socket.receive(receivePacket);
				
				//3.수신 데이터 출력
				byte[] data = receivePacket.getData();
				int length = receivePacket.getLength();
				String message = new String(data, 0, length, "UTF-8");
				if("".equals(message)) {
					message = "time request";
					sendTime(socket, receivePacket);
				}
				
				System.out.println("[server] received : " + message);
				
				if(!"time request".contentEquals(message)) {
					//4.데이터 보내기
					DatagramPacket sendPacket = new DatagramPacket(data, length,
							receivePacket.getAddress(), receivePacket.getPort());
					socket.send(sendPacket);
				}
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("packet 수신 오류");
		}
				
	}

	public static void sendTime(DatagramSocket socket, DatagramPacket receivePacket) {
		try {
			SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss a" );
			String date = format.format( Calendar.getInstance().getTime() );
			byte[] data = date.getBytes("UTF-8");
			int length = data.length;
			DatagramPacket sendPacket = new DatagramPacket(data,length,
					receivePacket.getAddress(), receivePacket.getPort());
			socket.send(sendPacket);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void consoleLog(String log) {
		System.out.println("[server] " + log);
	}
	

}
