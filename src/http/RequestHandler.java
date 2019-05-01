package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private static String documentRoot = "./webapp";
	
	static { // 클래스가 로딩될때 이부분이 실행된다 //항상 접근 가능 ?? 
		/*
		 * documentRoot = new File(RequestHandler. class. getProtectionDomain().
		 * getCodeSource(). getLocation(). toURI()). getPath(); documentRoot +=
		 * "/webapp"; System.out.println("---> " + documentRoot);
		 */
			
		documentRoot = RequestHandler.class.getClass().getResource("/webapp").getPath();
		
		InputStream is =  RequestHandler.class.getClass().getResourceAsStream("/webapp/index.html");
	}
	
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );
			
			// get IOStream 
			OutputStream os = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			
			String request = null;
			
			while(true) {
				String line = br.readLine();
				
				//브라우저가 연결을 끊으면... 
				if(line == null) {
					break;
				}
				
				//header 9줄 마지막 개행 , 그다음 바디
				//Request Header만 읽음
				if("".equals(line)) {
					break;
				}
				
				
				//Header의 첫벗째 라인만 처리
				if(request == null) {
					request = line;
				}
				
			}
			
			//consoleLog("request:" + request);
			String[] tokens =request.split(" ");
	
			if("GET".contentEquals(tokens[0])) {
				consoleLog("request:" + tokens[1]);
				responseStaticResource(os, tokens[1], tokens[2]);//os, url, protocol&version
			} else { // POST, PUT, DELETE, HEAD, CONNECT 와 같은 METHOD는 무시
				
				response400Error(os, tokens[2]);
			}
			
			// 예제 응답입니다.
			// (character -> bytes => 톰캣은 해당 내용을 parsing해서 request 객체에 넣어 servlet에 전달) 
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			// os.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
			// os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );	// 빈 개행 : 헤더랑 바디 나눔 
			// os.write( "\r\n".getBytes() );
			// os.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );
		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}

	public void responseStaticResource(
			OutputStream os, 
			String url, 
			String protocol) throws IOException { //이 메소드 호출하는 데서 io예외 처리해야
		
		if("/".equals(url)) {
			url = "/index.html"; //웰컴파일 설정 보통 상용 was는 설정파일에서 설정
		}
		
		//FileInputStream fs = new FileInputStream( documentRoot + url);
		File file = new File( documentRoot + url);
		if(file.exists() == false) {
			//응답 예시
			// HTTP/1.1 404 File Not Found\r\n (r : carriage return(맨 앞으로) n : new line(다음라인으로) 리눅스는 rn 윈도우는 n안에 r 들어있음)
		 	// Content-Type : text/html; charset=utf-8\r\n
			// \r\n
			// HTML 에러 문서(./webapp/error/404.html)
			
			response404Error(os, protocol);
			return;
		} 
		
		//nio(new io) 
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
		
		//응답
		os.write( (protocol +  " 200 OK\r\n").getBytes( "UTF-8" ));
		os.write( ("Content-Type:" + contentType +"; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		os.write( ("\r\n").getBytes() );
		os.write( body );
		
		//응답
		
	}
	public void response400Error(OutputStream os,String protocol) throws IOException {
		File file = new File(documentRoot + "/error/400.html");

		//nio(new io) 
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
		
		os.write( (protocol + " 400 Bad Request File\r\n").getBytes("UTF-8"));
		os.write( ("Content-Type:" + contentType +"; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		os.write( ("\r\n").getBytes() );
		os.write( body );
	}
	
	public void response404Error(OutputStream os,String protocol) throws IOException {
		//응답 예시
		// HTTP/1.1 400 Bad Request\r\n 
	 	// Content-Type : text/html; charset=utf-8\r\n
		// \r\n
		// HTML 에러 문서(./webapp/error/404.html)
		File file = new File(documentRoot + "/error/404.html");

		//nio(new io) 
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
		
		os.write( (protocol + " 404 NOT FOUND FILE\r\n").getBytes("UTF-8"));
		os.write( ("Content-Type:" + contentType +"; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		os.write( ("\r\n").getBytes() );
		os.write( body );
		
	}
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
	

}
