package br.uefs.ecomp.servidor.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import br.uefs.ecomp.servidor.model.Pessoa;



public class TrataClientes implements Runnable{

	private Socket cliente; 

	public TrataClientes(Socket s) {
		this.cliente = s;
	}

	@Override
	public void run() {
		System.out.println("Nova conexão com o cliente " +cliente.getInetAddress().getHostAddress());
		
		try {
			
			DataInputStream inputDados = new DataInputStream(cliente.getInputStream());
			DataOutputStream outputDados = new DataOutputStream(cliente.getOutputStream());
			
			String pacote = (String) inputDados.readUTF();
			System.out.println("pacote: " +pacote);
			int acao =  retornaAcao(pacote);
			System.out.println("Acao: " +acao);
			
			switch (acao) {
			case 1:
				Pessoa novaPessoa = decodificaPessoa(pacote);
				System.out.println(novaPessoa.getNome());
				outputDados.writeInt(2);
				outputDados.close();
				break;

			default:
				break;
			}
			inputDados.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static int retornaAcao(String pacote) {
		String[] partes = pacote.split("-");
		return Integer.parseInt(partes[0]);
	}

	public static Pessoa decodificaPessoa(String pacote) {
		String[] acaoDados = pacote.split("-");
		String[] partes = acaoDados[1].split(";");
		
		Pessoa novaPessoa = new Pessoa(partes[0], Boolean.parseBoolean(partes[1]), partes[2]);
		return novaPessoa;
	}
	

}
