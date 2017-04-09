package br.uefs.ecomp.servidor.model;

import java.util.ArrayList;

public class Conta {
	
	private double numeroConta;
	private double saldo;
	private static ArrayList titulares;
	
	public Conta(Pessoa pessoa) {
		titulares = new ArrayList();
		titulares.add(pessoa);
		saldo = 0;
	}
	public static void adicionarTitular(Pessoa pessoa) {
		titulares.add(pessoa);
	}
	
}
