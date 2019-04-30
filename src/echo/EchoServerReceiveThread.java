package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;
	
	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		InetSocketAddress inetRemoteSocketAddress =	(InetSocketAddress)socket.getRemoteSocketAddress();
		String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
		int remoteHostPort = inetRemoteSocketAddress.getPort();
		EchoServer.log("connected by client[" + remoteHostAddress + ":" + remoteHostPort +	"]");
		
		try { 
			//4. IOStream 받아오기 
			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream(),"utf-8"));

			PrintWriter pr = new PrintWriter(
					new OutputStreamWriter(socket.getOutputStream(),"utf-8"), true);
			
			
			while(true) {
				// 5. 데이터 읽기 
				String data = br.readLine();
				if(data == null) {
					EchoServer.log("closed by client");
					break;
				}
				
				EchoServer.log("received: " + data);
				
				
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
				if(socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		//data 통신용 소켓
	}
	
}
