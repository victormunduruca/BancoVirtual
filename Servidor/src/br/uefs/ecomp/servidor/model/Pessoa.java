package br.uefs.ecomp.servidor.model;

public class Pessoa {

	private String nome;
	private boolean eJuridica;
	private String numeroRegistro;
	private String usuario;
	private String senha;
	private Endereco endereco;

	public Pessoa(String nome, boolean eJuridica, String numeroRegistro, String cep, String rua, String numero, String usuario, String senha) {
		this.nome = nome;
		this.seteJuridica(eJuridica);
		this.numeroRegistro = numeroRegistro;
		this.endereco = new Endereco(cep, rua, numero);
		this.setUsuario(usuario);
		this.setSenha(senha);
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


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}


	public boolean iseJuridica() {
		return eJuridica;
	}


	public void seteJuridica(boolean eJuridica) {
		this.eJuridica = eJuridica;
	}
}
