package br.uefs.ecomp.servidor.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.uefs.ecomp.servidor.exceptions.ContaInexistenteException;
import br.uefs.ecomp.servidor.exceptions.FalhaAutenticacaoException;
import br.uefs.ecomp.servidor.exceptions.PessoaExistenteException;
import br.uefs.ecomp.servidor.exceptions.SaldoInsuficienteException;
import br.uefs.ecomp.servidor.exceptions.TitularExistenteException;
import br.uefs.ecomp.servidor.exceptions.UsuarioInexistenteException;
import br.uefs.ecomp.servidor.model.Acao;
import br.uefs.ecomp.servidor.model.Conta;
import br.uefs.ecomp.servidor.model.Pessoa;


/**
 * Thread que trata dos clientes
 * @author victo
 *
 */
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

				DataInputStream inputDados = new DataInputStream(cliente.getInputStream()); //Streams de dados com determinado cliente
				DataOutputStream outputDados = new DataOutputStream(cliente.getOutputStream());

				System.out.println("Conexao com cliente: " + cliente.getRemoteSocketAddress().toString());
				String pacote = (String) inputDados.readUTF(); // Lê pacote do cliente
				System.out.println("pacote: " +pacote);
				int acao =  retornaAcao(pacote);
				System.out.println("Acao: " +acao);
				switch (acao) {
				case 1: //Cadastro Conta corrente
					Pessoa novaPessoa = decodificaPessoa(pacote);
					int numeroConta = 0;
					try {
						numeroConta = controller.cadastrarNovaConta(novaPessoa, 1);	//Método de cadastro 
					} catch (PessoaExistenteException e) {
						outputDados.writeInt(Acao.PESSOA_EXISTENTE); // Envia erro ao cliente, caso ocorra
						System.out.println("Cliente não cadastrado");
					}
					outputDados.writeInt(Acao.CADASTRO_SUCESSO); // Informa ao cliente, caso o cadastro seja realizado com sucesso
					outputDados.writeInt(numeroConta);
					break;
				case 2: //Cadastro Conta Poupanca
					int numeroContaPoupanca = 0;
					Pessoa novaPessoaPoupanca = decodificaPessoa(pacote);
					try {
						numeroContaPoupanca = controller.cadastrarNovaConta(novaPessoaPoupanca, 2);	
					} catch (PessoaExistenteException e) {
						outputDados.writeInt(Acao.PESSOA_EXISTENTE);
						e.printStackTrace();
					}
					outputDados.writeInt(Acao.CADASTRO_SUCESSO);
					outputDados.writeInt(numeroContaPoupanca);
					break;
				case 3:
					String[] acaoLogin = pacote.split("-"); //Divide a ação do resto do pacote
					String[] partesLogin = acaoLogin[1].split(";");//Divide os atributos do pacote dividos por ponto e vírgula
					try {
						controller.autenticarPessoa(partesLogin[0], partesLogin[1], partesLogin[2]); //Autentica pessoa
					} catch (UsuarioInexistenteException e) { //Retorna ao usuário o resultado da operação
						outputDados.writeInt(Acao.USUARIO_INEXISTENTE);
					} catch (FalhaAutenticacaoException e) {
						outputDados.writeInt(Acao.FALHA_LOGIN);
					} catch (ContaInexistenteException e) {
						outputDados.writeInt(Acao.CONTA_INEXISTENTE);
					}
					outputDados.writeInt(Acao.LOGIN_SUCESSO);
					break;
				case 4: 
					String[] acaoTransacao = pacote.split("-");//Divide a ação do resto do pacote
					String[] partesTransacao = acaoTransacao[1].split(";");//Divide os atributos do pacote dividos por ponto e vírgula
					try {
						Conta conta = controller.getConta(partesTransacao[0]); //Recebe objetos das contas de origem e destino
						System.out.println("Saldo origem (antes): "+conta.getSaldo());
						Conta contaFim = controller.getConta(partesTransacao[1]);
						System.out.println("Saldo destino (antes): "+contaFim.getSaldo());
						controller.transacao(partesTransacao[0], partesTransacao[1], Double.parseDouble(partesTransacao[2]));
						conta = controller.getConta(partesTransacao[0]);
						System.out.println("Saldo origem (depois): "+conta.getSaldo());
						contaFim = controller.getConta(partesTransacao[1]);
						System.out.println("Saldo destino (depois): "+contaFim.getSaldo());
					} catch (NumberFormatException e) { //Retorna ao usuário o resultado da operação por meio de um int
						outputDados.writeInt(Acao.ERRO);
					} catch (SaldoInsuficienteException e) {
						outputDados.writeInt(Acao.SALDO_INSUFICIENTE);
					} catch (ContaInexistenteException e) {
						outputDados.writeInt(Acao.CONTA_INEXISTENTE);
					}
					outputDados.writeInt(Acao.TRANSACAO_SUCESSO);
					break;
				case 5:
					String[] acaoDeposito = pacote.split("-");//Divide a ação do resto do pacote
					String[] partesDeposito = acaoDeposito[1].split(";");//Divide os atributos do pacote dividos por ponto e vírgula
					try {
						Conta conta = controller.getConta(partesDeposito[0]); // Recebe objeto conta
						System.out.println("Saldo antes: "+conta.getSaldo());
						controller.deposito(partesDeposito[0], Double.parseDouble(partesDeposito[1])); // Método de depósito
						conta = controller.getConta(partesDeposito[0]);
						System.out.println("Saldo depois: "+conta.getSaldo());
					} catch (NumberFormatException e) { //Retorna ao usuário o resultado da operação por meio de um int
						outputDados.writeInt(Acao.ERRO);
					} catch (ContaInexistenteException e) {
						outputDados.writeInt(Acao.CONTA_INEXISTENTE);
					}
					outputDados.writeInt(Acao.DEPOSITO_SUCESSO);
					break;
				case 6:
					String[] acaoTitular = pacote.split("-");//Divide a ação do resto do pacote
					String[] partesTitular = acaoTitular[1].split(";");//Divide os atributos do pacote dividos por ponto e vírgula
					Pessoa pessoa = decodificaPessoa(pacote); //Decodifica pessoa
					try {
						controller.cadastrarTitular(pessoa, partesTitular[7]); // Método de cadastro titular
					} catch (ContaInexistenteException e) {//Retorna ao usuário o resultado da operação por meio de um int
						outputDados.writeInt(Acao.CONTA_INEXISTENTE);
					} catch (TitularExistenteException e) {
						outputDados.writeInt(Acao.TITULAR_EXISTENTE);
					} 
					outputDados.writeInt(Acao.TITULAR_SUCESSO);
					break;
				default:
					break;
				}
				inputDados.close(); //Streams fechadas a cada ação do usuário
				outputDados.close();
				cliente.close(); //Cliente, também fechado
			} 
			catch (EOFException e) { //Tratando erro: Cliente não escolhe opção correta
				   System.out.println("Cliente Desconectado!");
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	/**
	 * Método que retorna a ação de um pacote
	 * @param Pacote
	 * @return Retorna int que representa a ação de um pacote 
	 */
	public static int retornaAcao(String pacote) {
		String[] partes = pacote.split("-");
		return Integer.parseInt(partes[0]);
	}
	/**
	 * Método que decodifica pessoa
	 * @param Pacote codificado
	 * @return Objeto pessoa criado a partir dos atributos do pacote
	 */
	public static Pessoa decodificaPessoa(String pacote) {
		String[] acaoDados = pacote.split("-");
		String[] partes = acaoDados[1].split(";");

		Pessoa novaPessoa = new Pessoa(partes[0], Boolean.parseBoolean(partes[1]), partes[2], partes[3], partes[4], partes[5], partes[6]);
		return novaPessoa;
	}



}
