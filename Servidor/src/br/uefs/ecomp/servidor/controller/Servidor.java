package br.uefs.ecomp.servidor.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.uefs.ecomp.servidor.model.Pessoa;

public class Servidor {
	public static void main(String[] args) throws IOException {
		
		ServerSocket servidor = new ServerSocket(12346);
		System.out.println("Porta aberta");
		
		
		while(true) {
			
			TrataClientes trata = new TrataClientes(servidor);
			new Thread(trata).start();
		}
	}
	
	
}
