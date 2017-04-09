package br.uefs.ecomp.servidor.controller;

import java.util.ArrayList;

import br.uefs.ecomp.servidor.model.Conta;
import br.uefs.ecomp.servidor.model.Pessoa;

public class Controller {
	
	private static Controller controller;
	private static ArrayList listaContas;
	
	private Controller() {
		listaContas = new ArrayList();
	}
	
	private static boolean cadastrarConta(Pessoa pessoa) {
		Conta novaConta = new Conta(pessoa);
		listaContas.add(novaConta);
		return true;
	}
	
	public static Controller getInstance() {

		if(controller == null) { 
			controller = new Controller();
		}
		return controller; 
	}
	
	
}
