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
import br.uefs.ecomp.servidor.exceptions.PessoaExistenteException;
import br.uefs.ecomp.servidor.exceptions.SaldoInsuficienteException;
import br.uefs.ecomp.servidor.exceptions.UsuarioInexistenteException;
import br.uefs.ecomp.servidor.model.Conta;
import br.uefs.ecomp.servidor.model.Pessoa;

public class Controller {
	
	private static Controller controller;
	private static ArrayList listaContas;
	private static ArrayList listaPessoas;
	
	private Controller() {
		listaContas = new ArrayList();
		listaPessoas = new ArrayList();
	}
	
	public static void cadastrarNovaConta(Pessoa pessoa, String tipo) throws PessoaExistenteException, IOException{
		if(!existePessoa(pessoa)) { // se só usar essa vez tirar método
			Conta novaConta = new Conta(pessoa);
			File escritaArquivo = new File("dados\\contas"+"\\"+novaConta.getNumeroConta()+".txt");
			FileOutputStream fos = new FileOutputStream(escritaArquivo);
			ObjectOutputStream escrever = new ObjectOutputStream(fos);
			escrever.writeObject(novaConta);
			escrever.close();
			fos.close();
			
			File escritaPessoa;
	
			escritaPessoa = new File("dados\\titulares"+"\\"+pessoa.getNumeroRegistro()+".txt");
			FileOutputStream fosPessoa = new FileOutputStream(escritaPessoa);
			ObjectOutputStream escreverPessoa = new ObjectOutputStream(fosPessoa);
			escreverPessoa.writeObject(pessoa);
			escreverPessoa.close();
			fosPessoa.close();
			
		} else {
			throw new PessoaExistenteException();
		}
		
	}
	// Implementar diferentes contas
	
	
//	public static void cadastrarNovaContaPoupanca(Pessoa pessoa) throws PessoaExistenteException{
//		if(!existePessoa(pessoa)) { // se só usar essa vez tirar método
//			ContaPoupanca novaConta = new ContaPoupanca(pessoa);
//			listaPessoas.add(pessoa);
//			listaContas.add(novaConta);
//		} else {
//			throw new PessoaExistenteException();
//		}	
//	}
	
	//cadastrar titular
	//deposito deposita(numero conta, 
	//transferencias 	
	
	public static Controller getInstance() {

		if(controller == null) { 
			controller = new Controller();
		}
		return controller; 
	}
	
	public static boolean existePessoa(Pessoa pessoa) {
//		String caminho = "dados\\titulares\\";
//		if(pessoa.eJuridica())
//			caminho = caminho + "juridica";
//		else 
//			caminho = caminho + "fisica";
		ArrayList<String> listaNomes = getListaArquivos("dados\\titulares");
		if(listaNomes.contains(pessoa.getNumeroRegistro()))
			return true;
		else 
			return false;
	}
	
	public static ArrayList<String> getListaArquivos(String caminho) {
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
	
//	public static Pessoa getPessoa(String usuario) throws UsuarioInexistenteException, IOException{
//		try {
//			File arquivo = new File("dados\\titulares\\"+usuario+".txt");
//			FileInputStream fis;
//			fis = new FileInputStream(arquivo);
//			ObjectInputStream entrada = new ObjectInputStream(fis);
//			Pessoa pessoa = (Pessoa) entrada.readObject();
//			System.out.println("CPF da pessoa encontrada:" +pessoa.getNumeroRegistro());
//			entrada.close();
//			return pessoa;
//		} catch (FileNotFoundException e) {
//			System.out.println("n achou arquivo");
//			throw new UsuarioInexistenteException();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			return null; //mudar
//		}
//	}
	public static Conta getConta(String numeroConta) throws IOException, ContaInexistenteException{
		try {
			File arquivo = new File("dados\\contas\\"+numeroConta+".txt");
			FileInputStream fis;
			fis = new FileInputStream(arquivo);
			ObjectInputStream entrada = new ObjectInputStream(fis);
			Conta conta = (Conta) entrada.readObject();
			entrada.close();
			return conta;
		} catch (FileNotFoundException e) {
			System.out.println("n achou arquivo");
			throw new ContaInexistenteException();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null; //mudar
		}
	}
	public static boolean autenticarPessoa(String numeroRegistro, String senha, String numeroConta) throws UsuarioInexistenteException, IOException, FalhaAutenticacaoException, ContaInexistenteException {
		Conta conta = getConta(numeroConta);
		System.out.println("Pegou numero get conta que é: " +conta.getNumeroConta());
		Pessoa titular = conta.getTitular(numeroRegistro);
		if(titular == null)
			throw new UsuarioInexistenteException();
		//System.out.println("senha do titular: "+titular.getSenha() +" Senha do login: " +senha);
		if(titular != null && titular.getSenha().equals(senha))
			return true;
		else 
			throw new FalhaAutenticacaoException();
	}
	
	public static void transacao(String numeroContaOrigem, String numeroContaFim, double valor) throws IOException, SaldoInsuficienteException, ContaInexistenteException {
		Conta contaOrigem = getConta(numeroContaOrigem);
		Conta contaFim = getConta(numeroContaFim);
		if(contaOrigem.getSaldo() - valor < 0) {
			throw new SaldoInsuficienteException();
		}
		contaOrigem.setSaldo(contaOrigem.getSaldo()-valor);
		contaFim.setSaldo(contaFim.getSaldo() + valor);
		atualizarConta(contaOrigem);
		atualizarConta(contaFim);
	}
	
	public static void deposito(String numeroConta, double valor) throws ContaInexistenteException, IOException {
		Conta conta = getConta(numeroConta);
		conta.setSaldo(conta.getSaldo()+valor);
		atualizarConta(conta);
	}

	public static void atualizarConta(Conta conta) {
		try {
			File escritaArquivo = new File("dados\\contas"+"\\"+conta.getNumeroConta()+".txt");
			FileOutputStream fos = new FileOutputStream(escritaArquivo);
			ObjectOutputStream escrever = new ObjectOutputStream(fos);
			escrever.writeObject(conta);
			escrever.flush();
			escrever.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
