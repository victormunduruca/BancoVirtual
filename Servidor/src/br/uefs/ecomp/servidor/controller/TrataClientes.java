package br.uefs.ecomp.servidor.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.uefs.ecomp.servidor.exceptions.PessoaExistenteException;
import br.uefs.ecomp.servidor.model.Acao;
import br.uefs.ecomp.servidor.model.ContaCorrente;
import br.uefs.ecomp.servidor.model.Pessoa;



public class TrataClientes implements Runnable{
	
	ServerSocket servidor;
	private Socket cliente; 
	private Controller controller;
	
	public TrataClientes(ServerSocket servidor) {
		try {
			this.cliente = servidor.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.servidor = servidor;
		controller = Controller.getInstance();
	}

	@Override
	public void run() {

		
		while(true) {
			try {
				
				System.out.println("Nova conexão com o cliente " +cliente.getInetAddress().getHostAddress());
				DataInputStream inputDados = new DataInputStream(cliente.getInputStream());
				DataOutputStream outputDados = new DataOutputStream(cliente.getOutputStream());
				
				String pacote = (String) inputDados.readUTF();
				System.out.println("pacote: " +pacote);
				int acao =  retornaAcao(pacote);
				System.out.println("Acao: " +acao);
				
				switch (acao) {
				case 1:
					int numeroConta = 0;
					Pessoa novaPessoa = decodificaPessoa(pacote);
					try {
						controller.cadastrarNovaConta(novaPessoa, "corrente");	
						outputDados.writeInt(Acao.CADASTRO_SUCESSO);
						System.out.println(novaPessoa.getNome()+novaPessoa.getNumeroRegistro()+novaPessoa.getEndereco().getCep()+novaPessoa.getEndereco().getNumero()+novaPessoa.getEndereco().getRua()+novaPessoa.getUsuario()+novaPessoa.getSenha());
						outputDados.close();
					} catch (PessoaExistenteException e) {
						outputDados.writeInt(10);
						System.out.println("Cliente n cadastrado");
					}
					
					
					break;
				case 2: 
					Pessoa novaPessoaPoupanca = decodificaPessoa(pacote);
					try {
						controller.cadastrarNovaConta(novaPessoaPoupanca, "poupanca");	
					} catch (PessoaExistenteException e) {
						outputDados.writeInt(20);
						e.printStackTrace();
					}
					outputDados.writeInt(Acao.CADASTRO_SUCESSO);
					System.out.println(novaPessoaPoupanca.getNome()+novaPessoaPoupanca.getNumeroRegistro()+novaPessoaPoupanca.getEndereco().getCep()+novaPessoaPoupanca.getEndereco().getNumero()+novaPessoaPoupanca.getEndereco().getRua()+novaPessoaPoupanca.getUsuario()+novaPessoaPoupanca.getSenha());
					outputDados.close();
					break;
				default:
					break;
				}
				inputDados.close();
				cliente = servidor.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static int retornaAcao(String pacote) {
		String[] partes = pacote.split("-");
		return Integer.parseInt(partes[0]);
	}

	public static Pessoa decodificaPessoa(String pacote) {
		String[] acaoDados = pacote.split("-");
		String[] partes = acaoDados[1].split(";");
		
		Pessoa novaPessoa = new Pessoa(partes[0], Boolean.parseBoolean(partes[1]), partes[2], partes[3], partes[4], partes[5], partes[6], partes[7]);
		return novaPessoa;
	}
	

}
