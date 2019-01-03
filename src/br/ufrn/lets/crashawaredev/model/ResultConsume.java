package br.ufrn.lets.crashawaredev.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultConsume extends SearchResult {

    @JsonProperty("hits")
    private List<Hits> hits = new ArrayList<Hits>();
    
    @JsonProperty("2")
    private Aggregations aggs;
    
    public static ResultConsume mock() {
    	ResultConsume mock = new ResultConsume();
    	
    	for(int i=0 ; i<4 ; i++) {
    	
	    	Hits item = new Hits();
	    	item.getSource().setClasseExcecao("ArqException");
	    	item.getSource().setRootCause("com.sun.star.io.IOException"+i);
	    	item.getSource().setDataHoraOperacao("December 28th 2018, 15:25:56.033");
	    	item.getSource().setMensagemExcecao("com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException: conversion failed: could not save output document");
	    	
	    	mock.getHits().add(item);
    	}
    	
    	return mock;
    }

    public List<Hits> getHits() {
        return this.hits;
    }

    public void setHits(List<Hits> hits) {
        this.hits = hits;
    }

	public Aggregations getAggs() {
		return aggs;
	}

	public void setAggs(Aggregations aggs) {
		this.aggs = aggs;
	}
    
}
