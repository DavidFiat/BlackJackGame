package model;

public class Event {

	private String type;
	private String aux;

	public Event(String type) {
		this.type = type;

	}

	public Event(String type, String aux) {
		this.type = type;
		this.setAux(aux);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAux() {
		return aux;
	}

	public void setAux(String aux) {
		this.aux = aux;
	}

}
