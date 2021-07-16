package join_sockets;

import javax.swing.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidor extends JFrame implements Runnable{
	
	public MarcoServidor(){
		
		setBounds(100,100,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);
		
		Thread mihilo = new Thread(this);
		mihilo.start();
		
		}
	
	private	JTextArea areatexto;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		ArrayList <String> listaIp = new ArrayList<String>();
		
		try {
			ServerSocket servidor = new ServerSocket(9999);
			
			String nike;
			String contacto;
			String mensaje;
			
			PaqueteEnvio paquete_recibido;
			
			while (true) {				
				Socket misocket = servidor.accept();
				
				
				
				ObjectInputStream paquetes_datos = new ObjectInputStream(misocket.getInputStream());
				
				paquete_recibido = (PaqueteEnvio) paquetes_datos.readObject();
				
				nike = paquete_recibido.getNike();
				contacto = paquete_recibido.getContacto();
				mensaje = paquete_recibido.getMensaje();
				//String mensaje_texto = flujo_entrada.readUTF();
				if (!mensaje.equals(" online")) {
					areatexto.append("\n Nick: " + nike + "\n Para: " + contacto + "\n Mensaje: " + mensaje);
					Socket enviaDestinatario = new Socket(contacto, 9090);
					ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
					paqueteReenvio.writeObject(paquete_recibido);
					
					paqueteReenvio.close();
					
					enviaDestinatario.close();
					
					misocket.close();				
					
				} else {
					//Detect Online.					
					InetAddress localizacion = misocket.getInetAddress();
					String ipRemota = localizacion.getHostAddress();
					//System.out.println("Online: " + ipRemota);
					listaIp.add(ipRemota);
					
					paquete_recibido.setIps(listaIp);
					
					for (String string : listaIp) {
						Socket enviaDestinatario = new Socket(string, 9090);
						ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
						paqueteReenvio.writeObject(paquete_recibido);
						
						paqueteReenvio.close();
						
						enviaDestinatario.close();
						
						misocket.close();	
					}
					
					//---------------------------------
				}
				
				
			}
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
