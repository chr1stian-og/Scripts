import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	private DataOutputStream output;
	private DataInputStream input;
	
	private JPanel panel;
	private JTextArea taChat;
	private JTextField tfInput;
	private JButton btSend, btExit;
	private Socket client;
	
	
	public static void main(String []args) throws Exception{
		//take username from user
		String name = JOptionPane.showInputDialog(null, "Enter your name: ", "Username",
				JOptionPane.PLAIN_MESSAGE);
		String servername = "localhost";
		
		new Client(name, servername);
	}
	
	public Client(String username, String servername) throws Exception{
		setTitle(username);//set the title for frame as username
		
		this.username = username;
		
		//socket para conectar com o servidor
		client = new Socket(servername, 42069);
		
		//objectos para ler e enviar mensagens, respectivamente
		input = new DataInputStream(client.getInputStream());
		output = new DataOutputStream(client.getOutputStream());
		
		
		//mandar o username para o serveridor reconhecer
		output.writeUTF(username);
		
		buildInterface();
		
		//criacao do thread para escutar por mensagens
		new MessagesThread().start();
	}
	
	public void buildInterface() {
		btSend = new JButton("send");
		btSend.setBackground(Color.green);
		
		btExit = new JButton("exit");
		btExit.setBackground(Color.red);
		
		taChat = new JTextArea();
		taChat.setRows(10);
		taChat.setColumns(15);
		taChat.setBackground(Color.LIGHT_GRAY);
		taChat.setEditable(false);
		
		tfInput = new JTextField(30);
		JScrollPane sp = new JScrollPane(taChat, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		add(sp, "Center");
		
		panel = new JPanel(new FlowLayout());
		panel.setBackground(Color.BLUE);
		panel.add(tfInput);
		panel.add(btSend);
		panel.add(btExit);
		
		//adicionar o painel com os componentes contidos no frame
		add(panel,"South");
		
		btSend.addActionListener(this);
		btExit.addActionListener(this);
		
		setSize(100, 250);
		setLocationRelativeTo(null);
		setVisible(true);
		pack();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		try{
			if (event.getSource() == btExit) {
				//tell the server to end the session
				output.writeUTF(username+" "+"out.");
				System.exit(0);
			}else {
				//send message to server
				output.writeUTF(tfInput.getText());
				taChat.append(tfInput.getText()+"\n");
				tfInput.setText("");
								
			}
		}catch(Exception e) {}
		
		
	}
	
	class MessagesThread extends Thread{
		public void run() {
			String line;
			try {
				while (true) {
					//receber uma mensagem e adicionar na textArea
					line = input.readUTF();
					System.out.println(line);
					taChat.append("localhost: "+line + "\n");
					
				}
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	
}


