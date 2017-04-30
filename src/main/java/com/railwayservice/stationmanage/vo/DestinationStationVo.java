package com.railwayservice.stationmanage.vo;

public class DestinationStationVo {
	private String station;
	
	private String cityId;
	
	private String arriveTime;
	
	private String stationNameAbbr;
	
	private String spell;
	
	private String stationId;
	
	public String getStationNameAbbr() {
		return stationNameAbbr;
	}

	public void setStationNameAbbr(String stationNameAbbr) {
		this.stationNameAbbr = stationNameAbbr;
	}

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
}
