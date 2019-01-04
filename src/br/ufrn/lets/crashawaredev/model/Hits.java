package br.ufrn.lets.crashawaredev.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hits {
	
	private static final String baseUrl = "http://kibana-producao.info.ufrn.br:5601/app/kibana#/doc/indice_log_erro_detalhado-*/indice_log_erro_detalhado-%s/log_erro_detalhado?id=%s";

	@JsonProperty("_id")
    private String id;
	
	@JsonProperty("_source")
	private Source source;
	
	public Hits() {
		source = new Source();
	}
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public String getLink() {
		return String.format(baseUrl, "2019", id);
	}
}
