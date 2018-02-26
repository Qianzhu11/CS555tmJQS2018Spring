package Project04;

import java.util.Comparator;

public class Individual {
	private String id;
	private String name;
	private String gender;
	private String brithday;
	private int age;
	private boolean alive;
	private String death;
	private String child;
	private String spouse;
	
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBrithday() {
		return brithday;
	}
	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public String getDeath() {
		return death;
	}
	public void setDeath(String death) {
		this.death = death;
	}
	public String getChild() {
		return child;
	}
	public void setChild(String child) {
		this.child = child;
	}
	public String getSpouse() {
		return spouse;
	}
	public void setSpouse(String spouse) {
		this.spouse = spouse;
	}
}

class SortIndividual implements Comparator<Individual> {

	@Override
	public int compare(Individual o1, Individual o2) {
		String s1 = o1.getId();
		String s2 = o2.getId();
		int i = Integer.parseInt(s1.substring(1));
		int j = Integer.parseInt(s2.substring(1));
		return i - j;
	}
	
}
