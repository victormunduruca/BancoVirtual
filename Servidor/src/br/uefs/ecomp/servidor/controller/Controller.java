package br.uefs.ecomp.servidor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import br.uefs.ecomp.servidor.exceptions.ContaInexistenteException;
import br.uefs.ecomp.servidor.exceptions.FalhaAutenticacaoException;
import br.uefs.ecomp.servidor.exceptions.PessoaExistenteException;
import br.uefs.ecomp.servidor.exceptions.SaldoInsuficienteException;
import br.uefs.ecomp.servidor.exceptions.TitularExistenteException;
import br.uefs.ecomp.servidor.exceptions.UsuarioInexistenteException;
import br.uefs.ecomp.servidor.model.Conta;
import br.uefs.ecomp.servidor.model.ContaCorrente;
import br.uefs.ecomp.servidor.model.ContaPoupanca;
import br.uefs.ecomp.servidor.model.Pessoa;
/**
 * Classe utilizada para realizar as operações do servidor
 * @author Victor Munduruca
 *
 */
public class Controller {
	
	private static Controller controller;
	
	private Controller() {
		
	}
	/**
	 * Método que realiza o cadastro de novas contas
	 * @param Pessoa associada à conta
	 * @param Tipo da conta
	 * @throws PessoaExistenteException
	 * @throws IOException
	 */
	public int cadastrarNovaConta(Pessoa pessoa, int tipo) throws PessoaExistenteException, IOException{
		int numeroConta = 0;
		if(!existePessoa(pessoa)) { //verifica se pessoa não existe
			if(tipo == 1) { // Se o tipo for um, cadastra conta corrente
				ContaCorrente novaConta = new ContaCorrente(pessoa); // Instancia nova conta corrente
				numeroConta = novaConta.getNumeroConta();
				File escritaArquivo = new File("dados\\contas"+"\\"+novaConta.getNumeroConta()+".txt");//Inicia rotina de escrever o objeto no arquivo
				FileOutputStream fos = new FileOutputStream(escritaArquivo);
				ObjectOutputStream escrever = new ObjectOutputStream(fos);
				escrever.writeObject(novaConta); //Escreve o objeto
				escrever.close(); // Fecha Streams
				fos.close();
			} else if(tipo == 2) { // Se for tipo 2, cadastra conta poupança
				ContaPoupanca novaConta = new ContaPoupanca(pessoa); //O processo é o mesmo anterior, só diferenciando o tipo de objeto
				numeroConta = novaConta.getNumeroConta();
				File escritaArquivo = new File("dados\\contas"+"\\"+novaConta.getNumeroConta()+".txt");
				FileOutputStream fos = new FileOutputStream(escritaArquivo);
				ObjectOutputStream escrever = new ObjectOutputStream(fos);
				escrever.writeObject(novaConta);
				escrever.close();
				fos.close();
			}
			
			File escritaPessoa = new File("dados\\titulares"+"\\"+pessoa.getNumeroRegistro()+".txt"); //Inicia rotina para a escrita do objeto pessoa na pasta especializada
			FileOutputStream fosPessoa = new FileOutputStream(escritaPessoa);
			ObjectOutputStream escreverPessoa = new ObjectOutputStream(fosPessoa);
			escreverPessoa.writeObject(pessoa); // Escreve o objeto pessoa
			escreverPessoa.close(); // Fecha Streams
			fosPessoa.close();	
		} else {
			throw new PessoaExistenteException();
		}
		return numeroConta;
	}
	/*
	 * Implementação do padrão Singleton
	 */
	public static Controller getInstance() {

		if(controller == null) { 
			controller = new Controller();
		}
		return controller; 
	}
	/**
	 * Método que retorna se pessoa existe nos arquivos ou não
	 * @param Pessoa que deseja se verificar
	 * @return Boolean representando a existência da pessoa nos arquivos
	 */
	public boolean existePessoa(Pessoa pessoa) {
		ArrayList<String> listaNomes = getListaArquivos("dados\\titulares"); // Método retorna a lista de arquivos encontrados na pasta
		if(listaNomes.contains(pessoa.getNumeroRegistro())) // Verifica se o nome da pessoa existe nos registros
			return true;
		else 
			return false;
	}
	/**
	 * Método que retorna a lista de nomes dos arquivos de uma determinada pasta, sem a extensão ".txt"
	 * @param Caminho do arquivo
	 * @return Lista de nomes dos arquivos
	 */
	public ArrayList<String> getListaArquivos(String caminho) {
		ArrayList<String> listaNomes = new ArrayList<String>();
		File diretorio = new File(caminho);
		File[] listaArquivos = diretorio.listFiles();
		for (int i = 0; i < listaArquivos.length; i++) {
			if(listaArquivos[i].isFile())
			{
				System.out.println(listaArquivos[i].getName().replace(".txt", ""));
				listaNomes.add(listaArquivos[i].getName().replace(".txt", ""));
			}
		}
		return listaNomes;
	}
	/**
	 * Método que retorna Conta
	 * @param Número da conta 
	 * @return Objeto conta com o número requisitado
	 * @throws IOException
	 * @throws ContaInexistenteException 
	 */
	public Conta getConta(String numeroConta) throws IOException, ContaInexistenteException{
		try {
			File arquivo = new File("dados\\contas\\"+numeroConta+".txt"); //Tenta ler o arquivo diretamente
			FileInputStream fis;
			fis = new FileInputStream(arquivo);
			ObjectInputStream entrada = new ObjectInputStream(fis);
			Conta conta = (Conta) entrada.readObject(); //Lê objeto
			entrada.close();
			return conta;
		} catch (FileNotFoundException e) {
			System.out.println("n achou arquivo");
			throw new ContaInexistenteException(); // Lança exceção caso não ache a conta
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null; 
		}
	}
	/**
	 * Método que autentica pessoa
	 * @param CPF/CNPJ
	 * @param senha
	 * @param numeroConta
	 * @return Boolean indicando se a pessoa foi autenticada ou não
	 * @throws UsuarioInexistenteException
	 * @throws IOException
	 * @throws FalhaAutenticacaoException
	 * @throws ContaInexistenteException
	 */
	public boolean autenticarPessoa(String numeroRegistro, String senha, String numeroConta) throws UsuarioInexistenteException, IOException, FalhaAutenticacaoException, ContaInexistenteException {
		Conta conta = getConta(numeroConta); //Recebe objeto conta, buscado no arquivo. É por ele que a autenticação circulará
		Pessoa titular = conta.getTitular(numeroRegistro); //Com base na conta, acha o titular, se existe
		if(titular == null) // Se não encontrar o usuário, lança exceção
			throw new UsuarioInexistenteException();
		if(titular != null && titular.getSenha().equals(senha)) // Se o usuário existir e a senha for igual, o usuário é logado
			return true;
		else 
			throw new FalhaAutenticacaoException(); // Exceção lançada ocorra outra falha
	}
	/**
	 * Método que realiza transação entre duas contas
	 * @param numeroContaOrigem
	 * @param numeroContaFim
	 * @param valor
	 * @throws IOException
	 * @throws SaldoInsuficienteException
	 * @throws ContaInexistenteException
	 */
	public void transacao(String numeroContaOrigem, String numeroContaFim, double valor) throws IOException, SaldoInsuficienteException, ContaInexistenteException {
		Conta contaOrigem = getConta(numeroContaOrigem); //Recebe objeto conta, buscado no arquivo
		Conta contaFim = getConta(numeroContaFim);//Recebe objeto conta, buscado no arquivo
		if(contaOrigem.getSaldo() - valor < 0) { //Caso o saldo seja insuficiente, é lançado uma exceção
			throw new SaldoInsuficienteException();
		}
		contaOrigem.setSaldo(contaOrigem.getSaldo()-valor); //O valor é debitado da conta de origem
		contaFim.setSaldo(contaFim.getSaldo() + valor); //O valor é adicionado a conta de destino
		atualizarConta(contaOrigem); // As duas contas são atualizadas
		atualizarConta(contaFim);
	}
	/**
	 * Método que realiza um depósito em determinada conta
	 * @param numeroConta
	 * @param valor
	 * @throws ContaInexistenteException
	 * @throws IOException
	 */
	public  void deposito(String numeroConta, double valor) throws ContaInexistenteException, IOException {
		Conta conta = getConta(numeroConta); // Recebe objeto conta, buscado no arquivo
		conta.setSaldo(conta.getSaldo()+valor); //Adiciona o valor do depósito à conta
		atualizarConta(conta); // Atualiza conta
	}
	/**
	 * Método que realiza a atualização das contas, no arquivo
	 * @param Conta a ser atualizada
	 */
	public synchronized void atualizarConta(Conta conta) {
		try {
			File escritaArquivo = new File("dados\\contas"+"\\"+conta.getNumeroConta()+".txt"); // Rotina stream de arquivos
			FileOutputStream fos = new FileOutputStream(escritaArquivo);
			ObjectOutputStream escrever = new ObjectOutputStream(fos);
			escrever.writeObject(conta); // Escreve o objeto
			escrever.flush();//Utiliza o método flush para melhorar desempenho
			escrever.close();//Fecha streams
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Métod que realiza o cadastro de um titular
	 * @param Novo titular a ser adicionado
	 * @param numeroConta
	 * @throws IOException
	 * @throws ContaInexistenteException
	 * @throws TitularExistenteException 
	 * @throws PessoaExistenteException 
	 */
	public void cadastrarTitular(Pessoa pessoa, String numeroConta) throws IOException, ContaInexistenteException, TitularExistenteException{
			Conta conta = getConta(numeroConta);
			conta.adicionarTitular(pessoa);
			atualizarConta(conta);
	}
}
