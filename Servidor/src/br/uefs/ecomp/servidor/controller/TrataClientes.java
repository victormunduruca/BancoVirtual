package br.uefs.ecomp.servidor.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.uefs.ecomp.servidor.exceptions.ContaInexistenteException;
import br.uefs.ecomp.servidor.exceptions.PessoaExistenteException;
import br.uefs.ecomp.servidor.exceptions.SaldoInsuficienteException;
import br.uefs.ecomp.servidor.exceptions.UsuarioInexistenteException;
import br.uefs.ecomp.servidor.model.Acao;
import br.uefs.ecomp.servidor.model.Conta;
import br.uefs.ecomp.servidor.model.ContaCorrente;
import br.uefs.ecomp.servidor.model.Pessoa;



public class TrataClientes implements Runnable{

	ServerSocket servidor;
	private Socket cliente; 
	private Controller controller;

	public TrataClientes(Socket cliente) {
		this.cliente = cliente;
		controller = Controller.getInstance();
	}

	@Override
	public void run() {
		
			try {

				DataInputStream inputDados = new DataInputStream(cliente.getInputStream());
				DataOutputStream outputDados = new DataOutputStream(cliente.getOutputStream());

				System.out.println("Nova conexão com o cliente " +cliente.getInetAddress());
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
						System.out.println(novaPessoa.getNome()+novaPessoa.getNumeroRegistro()+novaPessoa.getEndereco().getCep()+novaPessoa.getEndereco().getNumero()+novaPessoa.getEndereco().getRua()+"Senha:"+novaPessoa.getSenha());
						//						outputDados.close();
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
					System.out.println(novaPessoaPoupanca.getNome()+novaPessoaPoupanca.getNumeroRegistro()+novaPessoaPoupanca.getEndereco().getCep()+novaPessoaPoupanca.getEndereco().getNumero()+novaPessoaPoupanca.getEndereco().getRua()+"Senha: "+novaPessoaPoupanca.getSenha());
					//					outputDados.close();
					break;
				case 3:
					String[] acaoLogin = pacote.split("-");
					String[] partesLogin = acaoLogin[1].split(";");
					try {
						System.out.println("Usuario: " +partesLogin[0] +"Senha: "+partesLogin[1]);
						controller.autenticarPessoa(partesLogin[0], partesLogin[1], partesLogin[2]);
					} catch (UsuarioInexistenteException e) {
						outputDados.writeInt(Acao.USUARIO_INEXISTENTE);
					} catch (FalhaAutenticacaoException e) {
						outputDados.writeInt(Acao.FALHA_LOGIN);
					} catch (ContaInexistenteException e) {
						outputDados.writeInt(Acao.CONTA_INEXISTENTE);
					}
					outputDados.writeInt(Acao.LOGIN_SUCESSO);
					break;
				case 4: 
					String[] acaoTransacao = pacote.split("-");
					String[] partesTransacao = acaoTransacao[1].split(";");
					try {
						Conta conta = controller.getConta(partesTransacao[0]);
						System.out.println("Saldo origem antes: "+conta.getSaldo());
						Conta contaFim = controller.getConta(partesTransacao[1]);
						System.out.println("Saldo fim antes: "+contaFim.getSaldo());
						controller.transacao(partesTransacao[0], partesTransacao[1], Double.parseDouble(partesTransacao[2]));
						conta = controller.getConta(partesTransacao[0]);
						System.out.println("Saldo origem depois: "+conta.getSaldo());
						contaFim = controller.getConta(partesTransacao[1]);
						System.out.println("Saldo fim antes: "+contaFim.getSaldo());
					} catch (NumberFormatException e) {
						outputDados.writeInt(Acao.ERRO);
					} catch (SaldoInsuficienteException e) {
						outputDados.writeInt(Acao.SALDO_INSUFICIENTE);
					} catch (ContaInexistenteException e) {
						outputDados.writeInt(Acao.CONTA_INEXISTENTE);
					}
					outputDados.writeInt(Acao.TRANSACAO_SUCESSO);
					break;
				case 5:
					String[] acaoDeposito = pacote.split("-");
					String[] partesDeposito = acaoDeposito[1].split(";");
					try {
						Conta conta = controller.getConta(partesDeposito[0]);
						System.out.println("Saldo antes: "+conta.getSaldo());
						controller.deposito(partesDeposito[0], Double.parseDouble(partesDeposito[1]));
						conta = controller.getConta(partesDeposito[0]);
						System.out.println("Saldo depois: "+conta.getSaldo());
						
					} catch (NumberFormatException e) {
						outputDados.writeInt(Acao.ERRO);
					} catch (ContaInexistenteException e) {
						outputDados.writeInt(Acao.CONTA_INEXISTENTE);
					}
					outputDados.writeInt(Acao.DEPOSITO_SUCESSO);
					break;
				default:
					//					inputDados.close();
					//					outputDados.close();
					break;
				}
				inputDados.close();
				outputDados.close();
				cliente.close();
				
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

		Pessoa novaPessoa = new Pessoa(partes[0], Boolean.parseBoolean(partes[1]), partes[2], partes[3], partes[4], partes[5], partes[6]);
		return novaPessoa;
	}



}
