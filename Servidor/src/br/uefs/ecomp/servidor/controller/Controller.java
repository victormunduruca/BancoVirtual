package br.uefs.ecomp.servidor.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import br.uefs.ecomp.servidor.exceptions.PessoaExistenteException;
import br.uefs.ecomp.servidor.model.Conta;
import br.uefs.ecomp.servidor.model.ContaPoupanca;
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
			File escritaArquivo = new File("dados\\contas\\corrente"+"\\"+novaConta.getNumeroConta()+".txt");
			FileOutputStream fos = new FileOutputStream(escritaArquivo);
			ObjectOutputStream escrever = new ObjectOutputStream(fos);
			escrever.writeObject(novaConta);
			escrever.close();
			fos.close();
			
			File escritaPessoa;
			if(pessoa.eJuridica())
				escritaPessoa = new File("dados\\titulares\\juridica"+"\\"+pessoa.getNumeroRegistro()+".txt");
			else 
				escritaPessoa = new File("dados\\titulares\\fisica"+"\\"+pessoa.getNumeroRegistro()+".txt");
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
	//autentica (login)
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
		String caminho = "dados\\titulares\\";
		if(pessoa.eJuridica())
			caminho = caminho + "juridica";
		else 
			caminho = caminho + "fisica";
		ArrayList<String> listaNomes = getListaArquivos(caminho);
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
	
	public static Pessoa getPessoa(String caminho) {
		
	}
}
