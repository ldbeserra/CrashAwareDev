package br.ufrn.lets.crashawaredev.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultConsume extends SearchResult {

    @JsonProperty("hits")
    @JsonDeserialize(using = SearchHitsDeserializer.class)
    private List<ResultConsumeItem> hits = new ArrayList<ResultConsumeItem>();
    
    public static ResultConsume mock() {
    	ResultConsume mock = new ResultConsume();
    	
    	for(int i=0 ; i<4 ; i++) {
    	
	    	ResultConsumeItem item = new ResultConsumeItem();
	    	item.setId("AWf2ftdz1FjN0tnxuq3t");
	    	item.setClasseExcecao("ArqException");
	    	item.setRootCause("com.sun.star.io.IOException");
	    	item.setDataHoraOperacao("December 28th 2018, 15:25:56.033");
	    	item.setMensagemExcecao("com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException: conversion failed: could not save output document");
	    	item.setMessage("2018-12-28T17:25:56.0336Z a360f0f5-7655-4348-8a4a-6ba954592a38 f24f0dc5-8325-4707-874c-a1a54df002b5 ERROR [br.ufrn.arq.log.legacy.erro.MovimentoErroDetalhado] 2018-12-28T17:25:56.0335Z 185653091 eliasj https://sipac.ufrn.br/sipac/protocolo/mesa_virtual/lista.jsf 1 3 ArqException #MSG#com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException: conversion failed: could not save output document#MSG#\n" + 
	    			"br.ufrn.arq.erros.ArqException: com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException: conversion failed: could not save output document\n" + 
	    			"	at br.ufrn.sipac.arq.util.ConversaoPDFUtils.converterFileToPDF(ConversaoPDFUtils.java:251)\n" + 
	    			"	at br.ufrn.sipac.protocolo.negocio.helper.ProtocoloHelper.documentoToPDF(ProtocoloHelper.java:2942)\n" + 
	    			"	at br.ufrn.sipac.protocolo.negocio.helper.ProtocoloHelper.gerarPDFProcesso(ProtocoloHelper.java:4626)\n" + 
	    			"	at br.ufrn.sipac.protocolo.negocio.helper.ProtocoloHelper.gerarPDFProcesso(ProtocoloHelper.java:4499)\n" + 
	    			"	at br.ufrn.sipac.protocolo.jsf.MesaVirtualMBean.gerarPDFProcesso(MesaVirtualMBean.java:7175)");
	    	
	    	
	    	mock.getHits().add(item);
    	}
    	
    	return mock;
    }

    public List<ResultConsumeItem> getHits() {
        return this.hits;
    }

    public void setHits(List<ResultConsumeItem> hits) {
        this.hits = hits;
    }
}
