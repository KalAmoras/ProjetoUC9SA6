package models;

public class Agendamento {

	private int id;
	private String date;
	private String time;

	public Agendamento() {
		super();
	}

	public Agendamento(int id, String date, String time) {
		super();
		this.id = id;
		this.date = date;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}