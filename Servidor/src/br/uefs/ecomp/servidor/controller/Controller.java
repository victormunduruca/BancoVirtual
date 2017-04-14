package br.uefs.ecomp.servidor.controller;

import java.util.ArrayList;

import br.uefs.ecomp.servidor.exceptions.PessoaExistenteException;
import br.uefs.ecomp.servidor.model.ContaCorrente;
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
	
	public static void cadastrarNovaContaCorrente(Pessoa pessoa) throws PessoaExistenteException{
		if(!existePessoa(pessoa)) { // se só usar essa vez tirar método
			ContaCorrente novaConta = new ContaCorrente(pessoa);
			listaPessoas.add(pessoa);
			listaContas.add(novaConta);
		} else {
			throw new PessoaExistenteException();
		}
		
	}
	public static void cadastrarNovaContaPoupanca(Pessoa pessoa) throws PessoaExistenteException{
		if(!existePessoa(pessoa)) { // se só usar essa vez tirar método
			ContaPoupanca novaConta = new ContaPoupanca(pessoa);
			listaPessoas.add(pessoa);
			listaContas.add(novaConta);
		} else {
			throw new PessoaExistenteException();
		}	
	}
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
		return listaPessoas.contains(pessoa);
	}
}
