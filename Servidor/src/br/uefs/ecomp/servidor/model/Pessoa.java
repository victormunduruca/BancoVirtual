package br.uefs.ecomp.servidor.model;

public class Pessoa implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nome;
	private boolean eJuridica;
	private String numeroRegistro;
	private String senha;
	private Endereco endereco;

	public Pessoa(String nome, boolean eJuridica, String numeroRegistro, String cep, String rua, String numero, String senha) {
		this.nome = nome;
		this.eJuridica = eJuridica;
		this.numeroRegistro = numeroRegistro;
		this.endereco = new Endereco(cep, rua, numero);
		this.setSenha(senha);
	}
	
	@Override
	public boolean equals(Object arg0) {
		Pessoa pessoaComp = (Pessoa) arg0;
		if(pessoaComp.getNumeroRegistro().equals(this.numeroRegistro)) 
			return true;
		return false;
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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}


	public boolean eJuridica() {
		return eJuridica;
	}


	public void setJuridica(boolean eJuridica) {
		this.eJuridica = eJuridica;
	}
}
