package join_sockets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;


public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setBounds(100,100,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		
		addWindowListener(new EnvioOnline());
	}	
	
}

//Envio de señal online.

class EnvioOnline extends WindowAdapter {
	
	public void windowOpened(WindowEvent e) {
		
		try {
			
			Socket misocket = new Socket("192.168.1.114", 9999);
			PaqueteEnvio datos = new PaqueteEnvio();
			datos.setMensaje(" online");
			ObjectOutputStream paqueteDatos = new ObjectOutputStream(misocket.getOutputStream());
			paqueteDatos.writeObject(datos);
			misocket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

//------------------


class LaminaMarcoCliente extends JPanel implements Runnable{
	
	public LaminaMarcoCliente(){
		
		String nick_usuario = JOptionPane.showInputDialog("Nick: ");
		
		JLabel n_nike = new JLabel("Nick: ");
		add(n_nike);
		nike = new JLabel();
		nike.setText(nick_usuario);
		add(nike);
	
		JLabel texto=new JLabel("Online: ");
		
		add(texto);

		contacto = new JComboBox<String>();
		add(contacto);
		campo2 = new JTextArea(12, 20);
		add(campo2);
		
	
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");
		EnviaText mievento = new EnviaText();
		
		miboton.addActionListener(mievento);
		
		add(miboton);
		
		Thread miHilo = new Thread(this);
		miHilo.start();
		
	}
	
	
	
	private class EnviaText implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			campo2.append("\n" + campo1.getText());
			
			try {
				Socket misocket = new Socket("192.168.1.114", 9999);
				PaqueteEnvio datos = new PaqueteEnvio();
				datos.setNike(nike.getText());
				datos.setContacto(contacto.getSelectedItem().toString());
				datos.setMensaje(campo1.getText());
				ObjectOutputStream paquetedatos = new ObjectOutputStream(misocket.getOutputStream());
				paquetedatos.writeObject(datos);
				misocket.close();
				
				//DataOutputStream flujo_salida = new DataOutputStream(misocket.getOutputStream());
				//flujo_salida.writeUTF(campo1.getText());				
				//flujo_salida.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
			}			
			
		}
		
		
	}
		
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		ServerSocket servidor_cliente;
		try {
			servidor_cliente = new ServerSocket(9090);
			Socket cliente;
			PaqueteEnvio paqueteRecibido;
			
			while (true) {
				cliente = servidor_cliente.accept();
				ObjectInputStream flujo_entrada = new ObjectInputStream(cliente.getInputStream());
				paqueteRecibido = (PaqueteEnvio) flujo_entrada.readObject();
				
				
				if (!paqueteRecibido.getMensaje().equals(" online")) {
					campo2.append("\n" + paqueteRecibido.getNike() + ": " + paqueteRecibido.getMensaje());
				} else {
					
					//campo2.append("\n" + paqueteRecibido.getIps());
					ArrayList<String> ipsMenu = new ArrayList<String>();
					
					ipsMenu = paqueteRecibido.getIps();
					
					contacto.removeAllItems();
					for (String string : ipsMenu) {
						contacto.addItem(string);
					}
				}
				
				
				
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private JTextField campo1;
	private JLabel nike;
	private JComboBox<String> contacto;
	private JTextArea campo2;	
	private JButton miboton;	
	
}


class PaqueteEnvio implements Serializable{
	
	private String nike;
	private String contacto;
	private String mensaje;
	
	private ArrayList<String> ips;
	
	public String getNike() {
		return nike;
	}
	public void setNike(String nike) {
		this.nike = nike;
	}
	public String getContacto() {
		return contacto;
	}
	public void setContacto(String contacto) {
		this.contacto = contacto;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public ArrayList<String> getIps() {
		return ips;
	}
	public void setIps(ArrayList<String> ips) {
		this.ips = ips;
	}
	
	
}


