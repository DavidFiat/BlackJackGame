package model;

public class Card {
	private String type = "Card";
	private String number;
	private String suit;

	public Card(String number, String suit) {
		this.setNumber(number);
		this.setSuit(suit);
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

}
