package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int PORT = 7000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			//1.서버소켓 생성
			serverSocket = new ServerSocket();
			
			//2.바인딩(binding)
			serverSocket.bind(new InetSocketAddress("0.0.0.0", PORT));
			
			//3. accept : 클라이언트의 연결요청을 기다린다.
			Socket socket = serverSocket.accept(); //blocking 
			
			InetSocketAddress inetRemoteSocketAddress =	(InetSocketAddress)socket.getRemoteSocketAddress();
			String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetRemoteSocketAddress.getPort();
			log("connected by client[" + remoteHostAddress + ":" + remoteHostPort +	"]");
			
			try { 
				//4. IOStream 받아오기 
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				BufferedReader br = new BufferedReader(
						new InputStreamReader(socket.getInputStream(),"utf-8"));
				
				//flush - true => auto flush 
				PrintWriter pr = new PrintWriter(
						new OutputStreamWriter(socket.getOutputStream(),"utf-8"), true);
				
				
				while(true) {
					// 5. 데이터 읽기 
					String data = br.readLine();
					if(data == null) {
						log("closed by client");
						break;
					}
					
					log("received: " + data);
					
					
					// 6. 데이터 쓰기
					pr.println(data);
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
			//data 통신용 소켓
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
		//server 소켓
	}

	public static void log(String log) {
		System.out.println("[server] " + log);
	}
}
