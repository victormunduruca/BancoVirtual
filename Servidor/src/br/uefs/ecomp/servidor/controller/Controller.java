package br.uefs.ecomp.servidor.controller;

import java.util.ArrayList;

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
	
	private static boolean cadastrarNovaConta(Pessoa pessoa) {
		if(!existePessoa(pessoa)) { // se só usar essa vez tirar método
			Conta novaConta = new Conta(pessoa);
			listaPessoas.add(pessoa);
			listaContas.add(novaConta);
			return true;
		}
		return false;
	}
	//cadastrar titular
	//deposito
	//saque
	
	
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
