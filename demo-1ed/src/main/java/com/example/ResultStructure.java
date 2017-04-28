package com.example;

public class ResultStructure {

	String Status;
	String Masseage;

	public ResultStructure(String Status, String Masseage) {
		super();
		this.Status = Status;
		this.Masseage = Masseage;
	}

	public String getShopName() {
		return Status;
	}

	public void setShopName(String Status) {
		this.Status = Status;
	}

	public String getAddress() {
		return Masseage;
	}

	public void setAddress(String Masseage) {
		this.Masseage = Masseage;
	}

}
