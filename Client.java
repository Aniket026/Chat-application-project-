import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame
{
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    
    //declare component
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);

    //constructor
    public Client()
    {
        try {

            System.out.println("sending request to server...");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("connection done.");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            //startWriting();

        }catch(Exception e){
            //TODO: handle exception
        }
    }
    
    private void handleEvents() 
    {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("Key Released"+e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    //System.out.println("You have pressed enter button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me:"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

        });
    }

    private void createGUI()
    {
        //GUI code...
        this.setTitle("Client Messager[End]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        
        heading.setIcon(new ImageIcon("cl.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //to set the layout of frame
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        
        this.setVisible(true);
    }

    //start reading [method]
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
                System.out.println("Server termited the chat");
                JOptionPane.showMessageDialog(this,"Server Terminated The Chat");
                messageInput.setEnabled(false);
                socket.close();
                break;
            }
            //System.out.println("Server :"+msg);
            messageArea.append("Server :" +msg+"\n");
        }
        }catch(Exception e){
            //e.printStackTrace();
            System.out.println("connection is closed");
        }

     };
     new Thread(r1).start();

    }

    //start writing [method] used for sending message
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
        System.out.println("this is client...going to start client");
        new Client();
    }
}