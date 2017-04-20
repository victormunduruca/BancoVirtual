package br.uefs.ecomp.servidor.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import br.uefs.ecomp.servidor.exceptions.TitularExistenteException;

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
		try {
			File file = new File("dados\\ultimo.txt");
			Scanner leitor;
			leitor = new Scanner(file);
			int idLido = leitor.nextInt();
			System.out.println("idLido" +idLido);
			leitor.close();
			idLido++;
			this.numeroConta = idLido;
			PrintWriter writer = new PrintWriter(file);
			writer.print(idLido);
			writer.close();
		} catch (FileNotFoundException e) {
			this.numeroConta = 0;
		}
		//idClasse++;
		//this.setNumeroConta(idClasse);
		titulares = new ArrayList();
		titulares.add(pessoa);
		setSaldo(0);
	}
	public  void adicionarTitular(Pessoa pessoa) throws TitularExistenteException {
		if(!titulares.contains(pessoa)) {
			titulares.add(pessoa);
		}
		else 
			throw new TitularExistenteException();
	}
	public int getNumeroConta() {
		return numeroConta;
	}
	public void setNumeroConta(int numeroConta) {
		this.numeroConta = numeroConta;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
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
