package eureka.json;

/**
 * Copyright (c) 2014, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Eureka is distributed under the terms of GNU GPL v3.0
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */
public class CategoryEntry {
	public String name;
	public String displaystackModID;
	public String displaystackName;
	public String displaystackType;

	public CategoryEntry(){

	}
	public CategoryEntry(String name, String displaystackModID, String displaystackName, String displaystackType){
		this.name = name;
		this.displaystackModID = displaystackModID;
		this.displaystackName = displaystackName;
		this.displaystackType = displaystackType;
	}
}