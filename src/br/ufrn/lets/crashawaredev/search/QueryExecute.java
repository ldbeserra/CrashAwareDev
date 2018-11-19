package br.ufrn.lets.crashawaredev.search;

import java.io.IOException;
import java.util.List;

import br.ufrn.arq.data.elasticsearch.query.QueryElasticsearchTemplateBase;
import br.ufrn.lets.crashawaredev.model.SearchForm;

public class QueryExecute extends QueryElasticsearchTemplateBase{

	private SearchForm searchForm;
	
	public QueryExecute(String urlConexaoElasticsearch, String nomeIndice, Class classe, SearchForm searchForm) {
		super(urlConexaoElasticsearch, nomeIndice, classe);
		this.searchForm = searchForm;
	}

	@Override
	public <T> List<T> executarQuery() throws IOException {
		return null;
	}

	public <T> T executarQueryPlain() throws IOException {
		return queryElasticsearchPlain(new QueryConsumer(searchForm));
	}
}
