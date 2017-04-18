package br.uefs.ecomp.servidor.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Conta implements java.io.Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int idClasse = 0;
	private int numeroConta;
	private double saldo;
	private ArrayList<Pessoa> titulares;
	
	public Conta(Pessoa pessoa) {
		idClasse++;
		this.setNumeroConta(idClasse);
		titulares = new ArrayList();
		titulares.add(pessoa);
		saldo = 0;
	}
	public  void adicionarTitular(Pessoa pessoa) {
		titulares.add(pessoa);
	}
	public int getNumeroConta() {
		return numeroConta;
	}
	public void setNumeroConta(int numeroConta) {
		this.numeroConta = numeroConta;
	}
	
	public ArrayList<Pessoa> getTitulares() {
		return this.titulares;
	}
	public void listarTitulares() {
		Iterator<Pessoa> it = (Iterator) titulares.iterator();
		while(it.hasNext()) {
			Pessoa pessoa = (Pessoa) it.next();
			
			System.out.println("Numero Registro pessoa na lista de conta:" +pessoa.getNumeroRegistro());
		}
	}
	public Pessoa getTitular(String numeroRegistro) {
		Iterator<Pessoa> it = (Iterator) titulares.iterator();
		while(it.hasNext()) {
			Pessoa pessoa = (Pessoa) it.next();
			if(pessoa.getNumeroRegistro().equals(numeroRegistro))
				return pessoa;
		}
		return null;
	}
	
}
