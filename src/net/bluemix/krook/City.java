package net.bluemix.krook;

public class City {

	private String id;
	private String name;
	private String countrycode;
	private String district;
	private int population;

	public City(String id, String name, String countrycode, String district, int population) {
		super();
		this.id = id;
		this.name = name;
		this.countrycode = countrycode;
		this.district = district;
		this.population = population;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

}
