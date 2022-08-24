import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;



public class Server extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//vectores para armazenar os clientes e os seus respectivos usernames
	private Vector <String> users = new Vector<String>();
	private Vector <GerirClientes> clients = new Vector<GerirClientes>();
	
	private JPanel panel;
	private JTextArea taChat;
	private JTextField tfInput;
	private JButton btSend, btExit;
	private Socket client;
	
	private DataOutputStream output;
	private DataInputStream input;
	
	public static void main(String[] args) throws Exception {
		new Server().process();
	}
	 
	public void process() throws Exception{
		//criacao e inicializacao do servidor
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(42069, 10);
		System.out.println("Server Started...");
		System.out.println("Waiting for connection...");
		while (true){
			//objecto para aceitar conexoes e gerir os clientes
			client = server.accept();
			
			if (client.isConnected())
				System.out.println("Client connected ");
				buildInterface();
			
			GerirClientes c = new GerirClientes(client);
			clients.add(c);
		}
	}
	
	public void buildInterface() {
		setTitle("localhost");
		
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
		panel.setBackground(Color.gray);
		panel.add(tfInput);
		panel.add(btSend);
		panel.add(btExit);
		
		//adicionar o painel com os componentes contidos no frame
		add(panel,"South");
		
		btSend.addActionListener(this);
		btExit.addActionListener(this);
		
		setSize(100, 250);
		setVisible(true);
		pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		try{
			if (event.getSource() == btExit){
				System.exit(0);
			}else {
				output.writeUTF(tfInput.getText());
				taChat.append(tfInput.getText()+"\n");
				tfInput.setText("");
								
			}
		}catch(Exception e) {}
	}
	
	public void broadcast(String user, String message) throws IOException {
		for (GerirClientes c : clients) {
			if (!c.getsUserName().equals(user)) {c.sendMessage(user, message);}
		}
	}
	
	
	//classe para gerir os clientes
	class GerirClientes extends Thread{
		private String u_name;
		private Server ser;
	
		public GerirClientes(Socket client) throws Exception {
			ser = new Server();
			
			//receber input do cliente
			input = new DataInputStream(client.getInputStream());
			
			//criar output para os clientes
			output = new DataOutputStream(client.getOutputStream());
			
			this.u_name = input.readUTF();
			ser.users.add(this.u_name);
			
			//iniciar thread
			start();
		}
		
		public void sendMessage(String username, String msg) throws IOException {
			output.writeUTF(username + ":" + msg);
		}
		
		public String getsUserName() {return u_name;}
		
		
		public void run() {			
			String line;

			try {
				while(true) {
					line = input.readUTF();
					System.out.println(line);
					taChat.append("client"+ ": "+line+"\n");
					//se o cliente inserir "end" ele e removido do chat
					if (line.equalsIgnoreCase("end")){
						ser.clients.remove(this);
						ser.users.remove(this.u_name);
						break;
					}
					
					//aqui nos enviamos a mensagem contida na variavel "line" para o cliente "u_name"
					ser.broadcast(u_name, line);
					
				}
			}catch(Exception e){}
					
		}
		
	}

}

