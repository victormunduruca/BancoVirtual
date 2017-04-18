package br.uefs.ecomp.servidor.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	public static void main(String[] args) throws IOException {
		
		ServerSocket servidor = new ServerSocket(12346);
		System.out.println("Porta aberta novo");
		while(true) {
			Socket cliente = servidor.accept();	

		
			TrataClientes trata = new TrataClientes(cliente);
			new Thread(trata).start();
		}
	}
	
	
}
