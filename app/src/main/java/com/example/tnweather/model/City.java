package com.example.tnweather.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class City implements Serializable {

	@SerializedName("country")
	private String country;

	@SerializedName("coord")
	private Coord coord;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setCoord(Coord coord){
		this.coord = coord;
	}

	public Coord getCoord(){
		return coord;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}