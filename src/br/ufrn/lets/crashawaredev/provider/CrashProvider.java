package br.ufrn.lets.crashawaredev.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufrn.lets.crashawaredev.model.Crash;
import br.ufrn.lets.crashawaredev.model.ResultConsume;
import br.ufrn.lets.crashawaredev.model.SearchForm;
import br.ufrn.lets.crashawaredev.search.QueryExecute;

public enum CrashProvider {
	
	INSTANCE;
	
	private final String host = "http://elasticsearch-manutencao.info.ufrn.br:9200";
	
	public ResultConsume getCrashesByClassName(String className) throws IOException{
		SearchForm form = new SearchForm();
		form.setClassName(className);
		
//		ResultConsume resultConsume = new QueryExecute(host, "indice_log_erro_detalhado_novo_teste-2017", ResultConsume.class, form).executarQueryPlain();
//		return resultConsume;
		return ResultConsume.mock();
	}
	
	public ResultConsume getAllCrashes() throws IOException{
		
//		ResultConsume resultConsume = new QueryExecute(host, "indice_log_erro_detalhado_novo_teste-2017", ResultConsume.class).executarQueryPlain();
//		return resultConsume;
		return ResultConsume.mock();
	}
	
}
