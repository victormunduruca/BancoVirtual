package br.uefs.ecomp.servidor.model;

public interface Acao {
	int CADASTRO_SUCESSO = 1;
	int FALHA_CADASTRO = 10;
	int USUARIO_INEXISTENTE = 30;
	int FALHA_LOGIN = 31;
	int CONTA_INEXISTENTE = 32;
	int LOGIN_SUCESSO = 3;
	int TRANSACAO = 4;
	int TRANSACAO_SUCESSO = 40;
	int SALDO_INSUFICIENTE = 41;
	int ERRO = 42;
	int DEPOSITO_SUCESSO = 50;
}
