package br.ufrn.lets.crashawaredev.search;

import br.ufrn.arq.data.elasticsearch.gerador.GeradorQueryElasticsearch;
import br.ufrn.lets.crashawaredev.model.SearchForm;

public class QueryConsumer implements GeradorQueryElasticsearch  {
	
	private final String TEMPLATE_PAGINATION = "\"from\": %d, \"size\" : %d,\n";

	private final String TEMPLATE_QUERY_FILTER = "{"+
            TEMPLATE_PAGINATION+
				"\"query\" : {\n"+
					"\"bool\" : {\n"+
						"\"filter\" :\n"+
						"[\n"+
							"%s"+
						"]\n"+
					"}\n"+
				"},%s \n"+
			"}";
	
	private final String TEMPLATE_FILTER_WILDCARD = "{\"wildcard\": { \"%s\" : \"*%s*\" }},\n";
	
	private final String TEMPLATE_FILTER_RANGE = "{\"range\" : {\"%s\" : {\"gte\": \"%s\",\"lte\": \"%s\"}}},\n";
	
	private final String TEMPLATE_TERM = "{\"term\": {\"%s\": \"%s\"}},\n";
	
	private final String TEMPLATE_AGGS = "\"aggs\":{\n" + 
			"      \"2\":{\n" + 
			"         \"terms\":{\n" + 
			"            \"field\":\"%s\",\n" + 
			"            \"size\":%s,\n" + 
			"            \"order\":{\n" + 
			"               \"_count\":\"desc\"\n" + 
			"            }\n" + 
			"         }\n" + 
			"      }\n" + 
			"   }";
	
	private final String TEMPLATE_SORT = "\"sort\" : [ {\"%s\" : {\"order\" : \"%s\"}}]";
	
	private SearchForm searchForm;
	
	public QueryConsumer(SearchForm searchForm) {
		super();
		this.searchForm = searchForm;
	}
	
	@Override
	public String gerarQuery() {
		StringBuilder criteriosConsulta = new StringBuilder();
		
		criteriosConsulta.append(String.format(TEMPLATE_TERM, "id_sistema", searchForm.getIdSistema()));

		criteriosConsulta.append(String.format(TEMPLATE_FILTER_RANGE, "data_hora_operacao", "now-" + searchForm.getDays() + "d/d", "now+1d/d"));
		
		if(exists(searchForm) && exists(searchForm.getSearchTerm()))
			criteriosConsulta.append(String.format(TEMPLATE_FILTER_WILDCARD, "stacktrace", unCapitalize(searchForm.getSearchTerm())));
		
		if(criteriosConsulta.toString().endsWith(",\n"))
			criteriosConsulta.replace(criteriosConsulta.length() - 2, criteriosConsulta.length() - 1, "");
		
		final String SUFIX = (searchForm.isCount() ? 
								String.format(TEMPLATE_AGGS, "root_cause.raw", searchForm.getSize()) : 
								String.format(TEMPLATE_SORT, "data_hora_operacao", "desc")
							 );
		
		String query = String.format(TEMPLATE_QUERY_FILTER, 0, searchForm.getSize(), criteriosConsulta.toString(), SUFIX);
		
		return query;
	}

	public boolean exists(Object value){
		return value != null && !value.toString().isEmpty();
	}
	
	/**
	 * Substitui os caracteres maiúsculos por uma "?", pois o wildcard do elastic converte tudo p/ minúscula por padrão.
	 * @param s
	 * @return
	 */
	private String unCapitalize(String s) {
		String output = "";
		for (int i = 0; i < s.length(); i++) {
		    char c = s.charAt(i);
		    output += Character.isUpperCase(c) ? "?" : c; 
		}
		return output;
	}
}
