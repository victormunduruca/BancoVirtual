package br.uefs.ecomp.servidor.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.uefs.ecomp.servidor.model.Pessoa;

public class Servidor {
	public static void main(String[] args) throws IOException {
		
		ServerSocket servidor = new ServerSocket(12349);
		System.out.println("Porta aberta");
		
		
		while(true) {
			Socket cliente = servidor.accept();
			TrataClientes trata = new TrataClientes(cliente);
			new Thread(trata).start();
		}
	}
	
	
}
