package main.java.baha;


public class BibleResponseDTO {
	String book;
	String chapter;
	String verses;
	String text;
	String version;
	String bid;
	public String getBook() {
		return book;
	}
	public void setBook(String book) {
		this.book = book;
	}
	public String getChapter() {
		return chapter;
	}
	public void setChapter(String chapter) {
		this.chapter = chapter;
	}
	public String getVerses() {
		return verses;
	}
	public void setVerses(String verses) {
		this.verses = verses;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	@Override
	public String toString() {
		return "{\"book\":\"" + book + "\", \"chapter\":\"" + chapter + "\", \"verses\":\"" + verses + "\", \"text\":\"" + text
				+ "\", \"version\":\"" + version + "\", \"bid\":\"" + bid + "\"}";
	}
	
}
