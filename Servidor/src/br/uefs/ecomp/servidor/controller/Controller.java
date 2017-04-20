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
 * Classe utilizada para realizar as opera��es do servidor
 * @author Victor Munduruca
 *
 */
public class Controller {
	
	private static Controller controller;
	
	private Controller() {
		
	}
	/**
	 * M�todo que realiza o cadastro de novas contas
	 * @param Pessoa associada � conta
	 * @param Tipo da conta
	 * @throws PessoaExistenteException
	 * @throws IOException
	 */
	public int cadastrarNovaConta(Pessoa pessoa, int tipo) throws PessoaExistenteException, IOException{
		int numeroConta = 0;
		if(!existePessoa(pessoa)) { //verifica se pessoa n�o existe
			if(tipo == 1) { // Se o tipo for um, cadastra conta corrente
				ContaCorrente novaConta = new ContaCorrente(pessoa); // Instancia nova conta corrente
				numeroConta = novaConta.getNumeroConta();
				File escritaArquivo = new File("dados\\contas"+"\\"+novaConta.getNumeroConta()+".txt");//Inicia rotina de escrever o objeto no arquivo
				FileOutputStream fos = new FileOutputStream(escritaArquivo);
				ObjectOutputStream escrever = new ObjectOutputStream(fos);
				escrever.writeObject(novaConta); //Escreve o objeto
				escrever.close(); // Fecha Streams
				fos.close();
			} else if(tipo == 2) { // Se for tipo 2, cadastra conta poupan�a
				ContaPoupanca novaConta = new ContaPoupanca(pessoa); //O processo � o mesmo anterior, s� diferenciando o tipo de objeto
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
	 * Implementa��o do padr�o Singleton
	 */
	public static Controller getInstance() {

		if(controller == null) { 
			controller = new Controller();
		}
		return controller; 
	}
	/**
	 * M�todo que retorna se pessoa existe nos arquivos ou n�o
	 * @param Pessoa que deseja se verificar
	 * @return Boolean representando a exist�ncia da pessoa nos arquivos
	 */
	public boolean existePessoa(Pessoa pessoa) {
		ArrayList<String> listaNomes = getListaArquivos("dados\\titulares"); // M�todo retorna a lista de arquivos encontrados na pasta
		if(listaNomes.contains(pessoa.getNumeroRegistro())) // Verifica se o nome da pessoa existe nos registros
			return true;
		else 
			return false;
	}
	/**
	 * M�todo que retorna a lista de nomes dos arquivos de uma determinada pasta, sem a extens�o ".txt"
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
	 * M�todo que retorna Conta
	 * @param N�mero da conta 
	 * @return Objeto conta com o n�mero requisitado
	 * @throws IOException
	 * @throws ContaInexistenteException 
	 */
	public Conta getConta(String numeroConta) throws IOException, ContaInexistenteException{
		try {
			File arquivo = new File("dados\\contas\\"+numeroConta+".txt"); //Tenta ler o arquivo diretamente
			FileInputStream fis;
			fis = new FileInputStream(arquivo);
			ObjectInputStream entrada = new ObjectInputStream(fis);
			Conta conta = (Conta) entrada.readObject(); //L� objeto
			entrada.close();
			return conta;
		} catch (FileNotFoundException e) {
			System.out.println("n achou arquivo");
			throw new ContaInexistenteException(); // Lan�a exce��o caso n�o ache a conta
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null; 
		}
	}
	/**
	 * M�todo que autentica pessoa
	 * @param CPF/CNPJ
	 * @param senha
	 * @param numeroConta
	 * @return Boolean indicando se a pessoa foi autenticada ou n�o
	 * @throws UsuarioInexistenteException
	 * @throws IOException
	 * @throws FalhaAutenticacaoException
	 * @throws ContaInexistenteException
	 */
	public boolean autenticarPessoa(String numeroRegistro, String senha, String numeroConta) throws UsuarioInexistenteException, IOException, FalhaAutenticacaoException, ContaInexistenteException {
		Conta conta = getConta(numeroConta); //Recebe objeto conta, buscado no arquivo. � por ele que a autentica��o circular�
		Pessoa titular = conta.getTitular(numeroRegistro); //Com base na conta, acha o titular, se existe
		if(titular == null) // Se n�o encontrar o usu�rio, lan�a exce��o
			throw new UsuarioInexistenteException();
		if(titular != null && titular.getSenha().equals(senha)) // Se o usu�rio existir e a senha for igual, o usu�rio � logado
			return true;
		else 
			throw new FalhaAutenticacaoException(); // Exce��o lan�ada ocorra outra falha
	}
	/**
	 * M�todo que realiza transa��o entre duas contas
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
		if(contaOrigem.getSaldo() - valor < 0) { //Caso o saldo seja insuficiente, � lan�ado uma exce��o
			throw new SaldoInsuficienteException();
		}
		contaOrigem.setSaldo(contaOrigem.getSaldo()-valor); //O valor � debitado da conta de origem
		contaFim.setSaldo(contaFim.getSaldo() + valor); //O valor � adicionado a conta de destino
		atualizarConta(contaOrigem); // As duas contas s�o atualizadas
		atualizarConta(contaFim);
	}
	/**
	 * M�todo que realiza um dep�sito em determinada conta
	 * @param numeroConta
	 * @param valor
	 * @throws ContaInexistenteException
	 * @throws IOException
	 */
	public  void deposito(String numeroConta, double valor) throws ContaInexistenteException, IOException {
		Conta conta = getConta(numeroConta); // Recebe objeto conta, buscado no arquivo
		conta.setSaldo(conta.getSaldo()+valor); //Adiciona o valor do dep�sito � conta
		atualizarConta(conta); // Atualiza conta
	}
	/**
	 * M�todo que realiza a atualiza��o das contas, no arquivo
	 * @param Conta a ser atualizada
	 */
	public synchronized void atualizarConta(Conta conta) {
		try {
			File escritaArquivo = new File("dados\\contas"+"\\"+conta.getNumeroConta()+".txt"); // Rotina stream de arquivos
			FileOutputStream fos = new FileOutputStream(escritaArquivo);
			ObjectOutputStream escrever = new ObjectOutputStream(fos);
			escrever.writeObject(conta); // Escreve o objeto
			escrever.flush();//Utiliza o m�todo flush para melhorar desempenho
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
	 * M�tod que realiza o cadastro de um titular
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
