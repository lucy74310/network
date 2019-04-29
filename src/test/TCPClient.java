package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

//클래스에 파이널이 붙으면 상속하지 말라는 것 
public class TCPClient {
	//자바에서는 상수가 없음. final이란 의미는 변수인데 값을 대입하는 것이 마지막. 
	private static final String SERVER_IP = "192.168.1.24";
	private static final int SERVER_PORT = 6001;
	
	
	//메소드에 파이널이 붙으면 메소드는 여기가 끝. 오버라이드 하지 말라는 것
	public static void main(String[] args) {
		Socket socket= null;
		try {
			//1. 소켓 생성
			socket= new Socket();
			
			//2. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[client] connected");
			
			//3. IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			//4. 쓰기
			String data = "Hello World\n";
			os.write(data.getBytes("utf-8"));
			
			//5. 읽기
			byte[] buffer = new byte[256];
			int readByteCount = is.read(buffer); 
			if(readByteCount == -1) {
				System.out.println("[client] closed by server ");
			}
			
			data = new String(buffer, 0, readByteCount, "utf-8");
			System.out.println("[client] received : " + data);
			
			
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

	}

}
