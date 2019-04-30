package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	private static final int PORT = 8000;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			//1.서버소켓 생성
			serverSocket = new ServerSocket();
			
			//2.바인딩(binding)
			serverSocket.bind(new InetSocketAddress("0.0.0.0", PORT));
			log("server starts...[port:" + PORT + "]");
			
			while(true) {
				//3. accept : 클라이언트의 연결요청을 기다린다.
				Socket socket = serverSocket.accept(); //지금은 blocking (단방향)
				//처리부분(readLine)과 어셉트 부분이 같이 돌게끔 바꿔야 한다. 
				//accept 하다가 read write오는 순간 쓰레드 하나 더 만들어서 소켓 넣어주고 start. 그 안에서 read write 
				
				Thread thread = new EchoServerReceiveThread(socket);
				thread.start();
			}
			
			//data 통신용 소켓
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				//두번 닫히면 에러남
				if(serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//server 소켓
	}

	public static void log(String log) {
		System.out.println("[server#" + Thread.currentThread().getId() + "]" + log);
	}
}
