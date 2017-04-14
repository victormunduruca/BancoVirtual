package br.uefs.ecomp.servidor.model;

import java.util.ArrayList;

public class Conta {
	
	private static int idClasse = 0;
	private double numeroConta;
	private double saldo;
	private static ArrayList titulares;
	
	public Conta(Pessoa pessoa) {
		idClasse++;
		this.numeroConta = idClasse;
		titulares = new ArrayList();
		titulares.add(pessoa);
		saldo = 0;
	}
	public static void adicionarTitular(Pessoa pessoa) {
		titulares.add(pessoa);
	}
	
}
