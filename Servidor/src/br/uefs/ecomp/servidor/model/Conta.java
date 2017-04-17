package br.uefs.ecomp.servidor.model;

import java.util.ArrayList;

public class Conta implements java.io.Serializable  {
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	private static int idClasse = 0;
	private int numeroConta;
	private double saldo;
	private static ArrayList titulares;
	
	public Conta(Pessoa pessoa) {
		idClasse++;
		this.setNumeroConta(idClasse);
		titulares = new ArrayList();
		titulares.add(pessoa);
		saldo = 0;
	}
	public static void adicionarTitular(Pessoa pessoa) {
		titulares.add(pessoa);
	}
	public int getNumeroConta() {
		return numeroConta;
	}
	public void setNumeroConta(int numeroConta) {
		this.numeroConta = numeroConta;
	}
	
}
