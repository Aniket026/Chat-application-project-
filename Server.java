import java.net.*;
import java.io.*;
public class Server
{

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //constructor..
    public Server()
    {
        try
        {
            server =new ServerSocket(7777);
            System.out.println("server is ready to accept connection");
            System.out.println("waiting...");
            socket=server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();


        }   catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    public void startReading()
    {
     //thread for reading data
     Runnable r1=()->{

        System.out.println("reader started..");

        try{
        while(true)
        {
            
            String msg=br.readLine();
            if(msg.equals("exit"))
            {
                System.out.println("Client termited the chat");
                socket.close();
                break;
            }
            System.out.println("Client :"+msg);
        }
        }catch(Exception e){
            //e.printStackTrace();
            System.out.println("connection is closed");
        }

     };
     new Thread(r1).start();



    }


    public void startWriting()
    {
     // thread for taking data from user and give it to the client
     Runnable r2=()-> {
        System.out.println("writer started...");
        try{
        while(!socket.isClosed())
        {
            
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                String content=br1.readLine();
                out.println(content);
                out.flush();
                if(content.equals("exit"))
                {

                    socket.close();
                    break;
                    
                    
                }


            } 
          
            }catch(Exception e){
                //TODO handle exception
                //e.printStackTrace();
                System.out.println("connection is closed");
            }
        };

     new Thread(r2).start();
}

    public static void main(String[] args){
        System.out.println("this is server..going to start server");
        new Server();
    }
}