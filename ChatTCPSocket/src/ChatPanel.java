    import javax.swing.*;
    import javax.swing.border.TitledBorder;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.io.BufferedReader;
    import java.io.DataOutputStream;
    import java.io.InputStreamReader;
    import java.net.Socket;

    public class ChatPanel extends JPanel {
           Socket socket=null;
           BufferedReader bf=null;
           DataOutputStream os=null;
           OutputThread t=null;
           String sender;
           String receiver;
           JTextArea txtMessages;


        public ChatPanel(Socket s, String sender, String receiver){
            setLayout(new BorderLayout(0,0));

            JPanel panel =new JPanel();
            panel.setBorder(new TitledBorder(null, "Message", TitledBorder.LEADING, TitledBorder.TOP,null,null));
            add(panel,BorderLayout.SOUTH);
            panel.setLayout(new GridLayout(1,2,0,0));

            JScrollPane scrollPane= new JScrollPane();
            panel.add(scrollPane);

            JTextArea txtMessage =new JTextArea();
            scrollPane.setViewportView(txtMessage);

            JButton btnSend = new JButton("Send");
           btnSend.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   if (txtMessage.getText().trim().length()==0) return;
                   try {
                   os.writeBytes(txtMessage.getText());
                   os.write(13);os.write(10);
                   os.flush();
                   txtMessages.append("\n"+sender+": "+txtMessage.getText());
                   txtMessage.setText("");
                   }catch (Exception ex){ex.printStackTrace();}
               }
           });

            JScrollPane scrollPane_1= new JScrollPane();
            add(scrollPane_1,BorderLayout.CENTER);

             txtMessages = new JTextArea();
            scrollPane_1.setViewportView(txtMessages);

            socket=s;
            this.sender=sender;
            this.receiver=receiver;
            try {
                bf=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                os =new DataOutputStream(socket.getOutputStream());
                t= new OutputThread(s,txtMessages,sender,receiver);
                t.start();
            }catch (Exception e){e.printStackTrace();}
        }
    public  JTextArea getTxtMessages(){
        return this.txtMessages;
    }
    }
