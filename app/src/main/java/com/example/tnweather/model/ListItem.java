package com.example.tnweather.model;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ListItem implements Serializable {

	@SerializedName("dt")
	private int dt;

	@SerializedName("rain")
	private Rain rain;

	@SerializedName("dt_txt")
	private String dtTxt;

	@SerializedName("weather")
	private List<WeatherItem> weather;

	@SerializedName("main")
	private Main main;

	@SerializedName("clouds")
	private Clouds clouds;

	@SerializedName("sys")
	private Sys sys;

	@SerializedName("wind")
	private Wind wind;

	public void setDt(int dt){
		this.dt = dt;
	}

	public int getDt(){
		return dt;
	}

	public void setRain(Rain rain){
		this.rain = rain;
	}

	public Rain getRain(){
		return rain;
	}

	public void setDtTxt(String dtTxt){
		this.dtTxt = dtTxt;
	}

	public String getDtTxt(){
		String data= "";
		String myinput = this.dtTxt;
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat output = new SimpleDateFormat("hh:mm a");
		/*String data = dtTxt.substring(11,16);
		return data;*/
		try {
			data = output.format(input.parse(myinput));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void setWeather(List<WeatherItem> weather){
		this.weather = weather;
	}

	public List<WeatherItem> getWeather(){
		return weather;
	}

	public void setMain(Main main){
		this.main = main;
	}

	public Main getMain(){
		return main;
	}

	public void setClouds(Clouds clouds){
		this.clouds = clouds;
	}

	public Clouds getClouds(){
		return clouds;
	}

	public void setSys(Sys sys){
		this.sys = sys;
	}

	public Sys getSys(){
		return sys;
	}

	public void setWind(Wind wind){
		this.wind = wind;
	}

	public Wind getWind(){
		return wind;
	}

	public String date(){
		String todaydate = dtTxt.substring(0,10);
		return todaydate;

	}
}