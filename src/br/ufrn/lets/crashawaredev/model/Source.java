package br.ufrn.lets.crashawaredev.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Source {
	
	@JsonProperty("classe_excecao")
    private String classeExcecao;

    @JsonProperty("mensagem_excecao")
    private String mensagemExcecao;

    @JsonProperty("root_cause")
    private String rootCause;
    
    @JsonProperty("data_hora_operacao")
    private String dataHoraOperacao;
    
	public String getClasseExcecao() {
		return classeExcecao;
	}

	public void setClasseExcecao(String classeExcecao) {
		this.classeExcecao = classeExcecao;
	}

	public String getMensagemExcecao() {
		return mensagemExcecao;
	}

	public void setMensagemExcecao(String mensagemExcecao) {
		this.mensagemExcecao = mensagemExcecao;
	}

	public String getDataHoraOperacao() {
		return dataHoraOperacao;
	}

	public void setDataHoraOperacao(String dataHoraOperacao) {
		this.dataHoraOperacao = dataHoraOperacao;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

}
