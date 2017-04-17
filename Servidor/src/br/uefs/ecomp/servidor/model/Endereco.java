package br.uefs.ecomp.servidor.model;

public class Endereco implements java.io.Serializable{

	private String cep;
	private String numero;
	private String rua;
	
	public Endereco(String cep, String rua, String numero) {
		this.cep = cep;
		this.rua = rua;
		this.numero = numero;
	}
	
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	
}
