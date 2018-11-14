package it.enhancer.enhancer;

public class LogCat {
	private String searchType;
	private String searchKw;
	private String interactionType;
	private String interactionParams;
	
	public LogCat(String searchType, String searchKw, String interactionType, String interactionParams) {
		this.searchType = searchType;
		this.searchKw = searchKw;
		this.interactionType = interactionType;
		this.interactionParams = interactionParams;
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

	public String getInteractionParams() {
		return interactionParams;
	}

	public void setInteractionParams(String interactionParams) {
		this.interactionParams = interactionParams;
	}
	
}
