package br.ufrn.lets.crashawaredev.util;

//TODO: organizar melhor as informações das exceções. Pode-se criar arquivos .properties ou inserir em banco de dados (assim pode-se trabalhar colaborativamente nas informações)
public class InfoExceptionsConstants {
	
	private static final String NULL_POINTER_EXCEPTION_INFO = "Em linhas gerais, o NullPointerException é uma exceção lançada pelo Java quando um programa tenta acessar um "
			+ "objeto de memória que não foi instanciado (ou melhor, inicializado) até o momento de sua chamada. O que isso quer dizer? "
			+ "O objeto ainda está nulo, ele não tem um valor definido.\n\n"
			+ "Fonte: https://www.devmedia.com.br/java-lang-nullpointerexception-dica/28677\n\n"
			+ "-------------- Atenção --------------\n\n"
			+ "-> Cuidado com a utilização de consultas com findByPrimaryKey(). Lembre-se sempre de verificar se o valor realmente foi retornado antes de referenciá-lo.\n\n"
			+ "-> Em classes utilitárias (Helper, Utils, etc) é recomendado sempre validar os argumentos passados se são válidos.";
	
	private static final String LAZY_INITIALIZATION_EXCEPTION_INFO = "É muito interessante ter relacionamentos LAZY: os dados são puxados apenas quando realmente necessários. "
			+ "Ao mesmo tempo deve-se tomar cuidado: pode gerar o problema das n+1 queries, e muitas vezes sabemos que tal relacionamento será tão utilizado, que deve ser feito "
			+ "de maneira EAGER, evitando mais uma query ser disparada ao banco de dados.\n" + 
			"\n" + 
			"Como exatamente funciona o relacionamento lazy? O Hibernate tira proveito de proxies dinâmicas: ele te devolve objetos que fingem ser listas nesse caso, "
			+ "e quando você invoca algum método deles, o Hibernate então faz a respectiva query para carregar o relacionamento. E há um momento em que isso falha: quando a "
			+ "sessão que carregou o objeto já estiver fechada!\n\n"
			+ "Fonte: http://blog.caelum.com.br/enfrentando-a-lazyinitializationexception-no-hibernate/";
	
	private static final String JSP_EXCEPTION = "Segundo a documentação da\n" + 
			"JspException, ela é uma exceção genérica " + 
			"conhecida pela engine JSP. Este tipo de exceção, porém, não pode\n" + 
			"ser definido como root cause " + 
			"da falha, pois geralmente encapsula outro tipo de exceção " + 
			"(IOException, NullPointerException, etc).\n\n"
			+ "-------------- Atenção --------------\n\n"
			+ "-> Cuidado ao capturar atributos da sessão com os métodos de getParameter(), getParameterInt() etc. É sempre bom validar se os valor foi realmente passado via request.\n"
			+ "-> Lembrem-se de adicionar os métodos get e set nos controladores. Às vezes uma JspException pode ser lançada devido à escrita incorreta do método (getId() é diferente de getID())";
			
	
	public static String getInfo(String exceptionName) {
		if(exceptionName.contains("NullPointerException"))
			return NULL_POINTER_EXCEPTION_INFO;
		if(exceptionName.contains("LazyInitializationException"))
			return LAZY_INITIALIZATION_EXCEPTION_INFO;
		if(exceptionName.contains("JspException"))
			return JSP_EXCEPTION;

		return "Nenhuma informação registrada para este tipo de exceção.";
	}

}
