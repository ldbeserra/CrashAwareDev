package br.ufrn.lets.crashawaredev.provider;

import java.io.IOException;

import br.ufrn.lets.crashawaredev.model.ResultConsume;
import br.ufrn.lets.crashawaredev.model.SearchForm;
import br.ufrn.lets.crashawaredev.search.QueryExecute;

public enum CrashProvider {
	
	INSTANCE;
	
//	private final String host = "http://elasticsearch01-producao.info.ufrn.br:9200";
	private final String host = "http://elasticsearch-manutencao.info.ufrn.br:9200";
	
	private final String index = "indice_log_erro_detalhado_novo_teste-2017";
//	private final String index = "indice_log_erro_detalhado-*";
	
	private final String FIELD_AGGREGATIONS = "aggregations";
	
	public ResultConsume getCrashesByTerm(String term) throws IOException{
		SearchForm form = new SearchForm();
		form.setSearchTerm(term);
		form.setSize(50);
		form.setDays(3);
		
		ResultConsume resultConsume = new QueryExecute(host, index, ResultConsume.class, form).executarQueryPlain();
		return resultConsume;
	}
	
	public ResultConsume getAllCrashes() throws IOException{
		SearchForm form = new SearchForm();
		form.setSearchTerm("");
		form.setSize(50);
		form.setDays(3);
		
		ResultConsume resultConsume = new QueryExecute(host, index, ResultConsume.class, form).executarQueryPlain();
		return resultConsume;
	}
	
	public Long getClassCrashCount(String term) throws IOException {
		SearchForm form = new SearchForm();
		form.setSearchTerm(term);
		form.setSize(0);
		form.setDays(3);
		
		ResultConsume resultConsume = new QueryExecute(host, index, ResultConsume.class, form).executarQueryPlain();
		return (resultConsume != null ? resultConsume.getTotal() : 0l);
	}
	
	public ResultConsume getTopCauses() throws IOException {
		SearchForm form = new SearchForm();
		form.setSearchTerm("");
		form.setSize(20);
		form.setDays(1000);
		form.setCount(true);
		
		ResultConsume resultConsume = new QueryExecute(host, index, ResultConsume.class, form, FIELD_AGGREGATIONS).executarQueryPlain();
		return resultConsume;
	}
	
}
