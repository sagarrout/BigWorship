package main.java.baha;

public class BibleRequestDTO {
	String book;
	String bid;
	String chapter;
	String chapter_roman;
	String verse;
	int found;
	String version;
	String next_chapter;
	public String getBook() {
		return book;
	}
	public void setBook(String book) {
		this.book = book;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getChapter() {
		return chapter;
	}
	public void setChapter(String chapter) {
		this.chapter = chapter;
	}
	public String getChapter_roman() {
		return chapter_roman;
	}
	public void setChapter_roman(String chapter_roman) {
		this.chapter_roman = chapter_roman;
	}
	public String getVerse() {
		return verse;
	}
	public void setVerse(String verse) {
		this.verse = verse;
	}
	public int getFound() {
		return found;
	}
	public void setFound(int found) {
		this.found = found;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getNext_chapter() {
		return next_chapter;
	}
	public void setNext_chapter(String next_chapter) {
		this.next_chapter = next_chapter;
	}
//	@Override
//	public String toString() {
//		return "{\"book\":\"" + book + "\", \"bid\":\"" + bid + "\", \"chapter\":\"" + chapter + "\", \"chapter_roman\":\""
//				+ chapter_roman + "\", \"verse\":\"" + verse + "\", \"found\":\"" + found + "\", \"version\":\"" + version + "\", \"next_chapter\":"
//				+ next_chapter + "\"}";
//	}
	
	
}
