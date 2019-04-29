package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			//1.서버소켓 생성
			serverSocket = new ServerSocket();
			
			//2.바인딩(binding)
			//	: Socket에 SocketAddress(IPAddress + Port)를 바인딩 한다.
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			//X
			//localhost가 -가 나오므로 다른 방식으로 바인딩
			//String localhost = inetAddress.getHostAddress();
			//serverSocket.bind(new InetSocketAddress(localhost, 5000));
			
			//O (telnet 127.0.0.1 5000)
			serverSocket.bind(new InetSocketAddress("0.0.0.0", 6001));
			
			//O (telnet 192.168.56.1 5000)
			//serverSocket.bind(new InetSocketAddress(inetAddress, 5000));
			
			
			//3. accept
			//	: 클라이언트의 연결요청을 기다린다.
			Socket socket = serverSocket.accept(); //blocking 
			
			//Socket은 InetSocketAddress의 부모 
			//부모에서 자식으로 downcasting 
			InetSocketAddress inetRemoteSocketAddress = 
					(InetSocketAddress)socket.getRemoteSocketAddress();
			
			//inetRemoteSocketAddress.getAddress() => inetAddress
			String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetRemoteSocketAddress.getPort();
			
			
			System.out.println(
				"[server] connected by client[" + 
				remoteHostAddress + ":" + 
				remoteHostPort + 
				"]" 
			);
			
			try { //데이터 받으면서 exception이 나올 확율이 더 많으니까 따로 
				//4. IOStream 받아오기 
				//SocketInputStream, SocketOutputStream 의 자식 
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				while(true) {
					//5.데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer);
					if(readByteCount == -1) {
						//클라이언트가 정상종료 한 경우 
						//close() 메소드 호출
						System.out.println("[server] closed by client");
						break;
					}
					
					String data = new String(buffer, 0, readByteCount , "utf-8");
					System.out.println("[server] received:" + data);
					
					//6. 데이터 쓰기 
					os.write(data.getBytes("utf-8"));
				}
				
			} catch(SocketException e) {
				System.out.println("[server] sudden cloased by client");
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				try {
					//두번 닫히면 에러남
					if(socket != null && !socket.isClosed())
					socket.close();
				} catch(IOException e) {
					
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				//두번 닫히면 에러남
				if(serverSocket != null && !serverSocket.isClosed())
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//2.
	}

}
