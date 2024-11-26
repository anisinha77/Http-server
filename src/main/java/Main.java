import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    //
     try {
       ServerSocket serverSocket = new ServerSocket(4221);

       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);

       try(Socket socket = serverSocket.accept()){
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           String request = bufferedReader.readLine();

           if(request != null){
               String[] requestParts = request.split(" ");
               OutputStream outputStream = socket.getOutputStream();
               if(requestParts.length >=2 && "GET".equals(requestParts[0])){
                   String path = requestParts[1];
                   String response;
                   if("/".equals(path)){
                       response = "HTTP/1.1 200 OK\r\n\r\n";
                       outputStream.write(response.getBytes());
                       outputStream.flush();
                   }
                   else if(path.startsWith("/echo/")){
                       String str = path.substring("/echo/".length());
                       byte[] responseBody = str.getBytes();
                       String responseHeaders = String.format(
                               "HTTP/1.1 200 OK\r\n" +
                                       "Content-Type: text/plain\r\n" +
                                       "Content-Length: %d\r\n\r\n",
                               responseBody.length);

                       outputStream.write(responseHeaders.getBytes());
                       outputStream.write(responseBody);
                       outputStream.flush();
                   }
                   else{
                       response = "HTTP/1.1 404 Not Found\r\n\r\n";
                       outputStream.write(response.getBytes());
                       outputStream.flush();
                   }
               }
           }
       }
       catch (IOException e){
           System.out.println("Exception : "+e);
       }


     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
