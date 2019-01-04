package br.ufrn.lets.crashawaredev.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe base de resultado da consulta no ElasticSearch
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult implements Serializable{

	private static final long serialVersionUID = -6901962794712510186L;
	
	@JsonProperty("total")
    private long total;

    public SearchResult() {
    }

    public SearchResult(long total) {
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
