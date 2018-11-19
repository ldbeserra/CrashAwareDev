package br.ufrn.lets.crashawaredev.model;

public class Crash {

	private String _id;
	private String message;
	private String mensagem_excecao;
	private String data_hora_operacao;
	
	public Crash() {}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMensagem_excecao() {
		return mensagem_excecao;
	}

	public void setMensagem_excecao(String mensagem_excecao) {
		this.mensagem_excecao = mensagem_excecao;
	}

	public String getData_hora_operacao() {
		return data_hora_operacao;
	}

	public void setData_hora_operacao(String data_hora_operacao) {
		this.data_hora_operacao = data_hora_operacao;
	}

}
