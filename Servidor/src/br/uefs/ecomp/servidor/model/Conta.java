package br.uefs.ecomp.servidor.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import br.uefs.ecomp.servidor.exceptions.TitularExistenteException;

/**
 * CLasse que representa uma Conta 
 * @author Victor Munduruca
 *
 */
public class Conta implements java.io.Serializable  {
	
	private static final long serialVersionUID = 1L;
	private int numeroConta;
	private double saldo;
	private ArrayList<Pessoa> titulares;
	
	public Conta(Pessoa pessoa) {
		try {
			File file = new File("dados\\ultimo.txt"); //O n�mero da conta � gerado automaticamente com base em um arquivo, que guarda o n�mero
			//da �ltima conta cadastrada, para evitar sobrescri��o de contas quando o sistema cair, por exemplo
			Scanner leitor;
			leitor = new Scanner(file);
			int idLido = leitor.nextInt();
			leitor.close();
			idLido++;
			this.numeroConta = idLido;
			PrintWriter writer = new PrintWriter(file);
			writer.print(idLido);
			writer.close();
		} catch (FileNotFoundException e) {
			this.numeroConta = 0;
		}
		titulares = new ArrayList<Pessoa>();
		titulares.add(pessoa); //Adiciona a pessoa a conta
		setSaldo(0);
	}
	/**
	 * M�todo que adiciona titular
	 * @param Titular 
	 * @throws TitularExistenteException
	 */
	public  void adicionarTitular(Pessoa pessoa) throws TitularExistenteException {
		if(!titulares.contains(pessoa)) { //Verifica se titular j� existe na lista de titulares
			titulares.add(pessoa); // Caso n�o exista, ele � cadastrado
		}
		else 
			throw new TitularExistenteException(); //Caso exista, essa exce��o � lan�adaa
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
	/**
	 * M�todo que itera e lista titulares
	 */
	public void listarTitulares() {
		Iterator<Pessoa> it = (Iterator) titulares.iterator();
		while(it.hasNext()) {
			Pessoa pessoa = (Pessoa) it.next();
			
			System.out.println("Numero Registro pessoa na lista de conta:" +pessoa.getNumeroRegistro());
		}
	}
	/**
	 * M�todo que retorna titular
	 * @param CPF/CNPJ do titular
	 * @return Titular
	 */
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
