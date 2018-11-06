package it.enhancer.enhancer;

public class LogCat {
	private String searchType;
	private String searchKw;
	public String interactionType;
	
	public LogCat(String searchType, String searchKw, String interactionType) {
		super();
		this.searchType = searchType;
		this.searchKw = searchKw;
		this.interactionType = interactionType;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchKw() {
		return searchKw;
	}

	public void setSearchKw(String searchKw) {
		this.searchKw = searchKw;
	}

	public String getInteractionType() {
		return interactionType;
	}

	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
	}
	
	
	
}
