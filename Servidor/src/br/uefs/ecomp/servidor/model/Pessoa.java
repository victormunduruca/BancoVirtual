package br.uefs.ecomp.servidor.model;

public class Pessoa {

	private String nome;
	private boolean eJuridica;
	private String numeroRegistro;
	
	public Pessoa(String nome, boolean eJuridica, String numeroRegistro) {
		this.nome = nome;
		this.eJuridica = eJuridica;
		this.numeroRegistro = numeroRegistro;
	}
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNumeroRegistro() {
		return numeroRegistro;
	}
	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}
}
