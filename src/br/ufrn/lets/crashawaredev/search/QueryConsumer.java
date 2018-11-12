package br.ufrn.lets.crashawaredev.search;

import br.ufrn.arq.data.elasticsearch.gerador.GeradorQueryElasticsearch;
import br.ufrn.lets.crashawaredev.model.SearchForm;

public class QueryConsumer implements GeradorQueryElasticsearch  {

	private final String TEMPLATE_TERM = "{\"term\": {\"%s\": \"%s\"}},\n";
	
	private SearchForm searchForm;
	
	public QueryConsumer(SearchForm searchForm) {
		super();
		this.searchForm = searchForm;
	}
	
	@Override
	public String gerarQuery() {
		StringBuilder criteriosConsulta = new StringBuilder();
		
		if(exists(searchForm.getClassName()))
			criteriosConsulta.append(String.format(TEMPLATE_TERM, "uniqueId", searchForm.getClassName()));
		
		return criteriosConsulta.toString();
	}

	public boolean exists(Object value){
		return value != null && !String.valueOf(value).isEmpty();
	}
}