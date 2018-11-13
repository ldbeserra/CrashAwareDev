package br.ufrn.lets.crashawaredev.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufrn.lets.crashawaredev.model.Crash;
import br.ufrn.lets.crashawaredev.search.QueryExecute;

public enum CrashProvider {
	
	INSTANCE;
	
	private List<Crash> crashes;

	private final String host = "http://hermod-es01-sustentacao.info.ufrn.br:9200";
	
	public List<Crash> getCrashesByClassName(String className) throws IOException{
		List<Crash> result = new QueryExecute(host, "indice_log_erro_detalhado-*", Crash.class).executarQueryPlain();
		return result;
	}
	
	private CrashProvider() {
		crashes = new ArrayList<>();
	}
	
	public List<Crash> getCrashes(){
		return crashes;
	}

}
