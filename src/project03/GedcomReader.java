package project03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GedcomReader {
	List<String> information;
	List<Individual> individuals;
	List<Family> families;
	Map<String, Individual> map;
	
	public void readFile(String file) {
		information = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
				information.add(s);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> sliceInformation(String s) {
		List<String> res = new ArrayList<String>();
		int i = 0;
		int j = 0;
		while (j < s.length()) {
			if (s.charAt(j) != ' ') {
				j++;
			} else {
				res.add(s.substring(i, j));
				i = ++j;
			}
		}
		res.add(s.substring(i, j));
		return res;
	}
	
	public void writeIndividual() {
		individuals = new ArrayList<Individual>();
		families = new ArrayList<Family>();
		map = new HashMap<String, Individual>();
		for (int i = 0; i < information.size(); i++) {
			List<String> list = sliceInformation(information.get(i));
			if (list.get(0).equals("0") && list.get(list.size() - 1).equals("INDI")) {
				Individual individual = new Individual();
				String id = list.get(1).replaceAll("@", "");
				individual.setId(id);
				map.put(id, individual);
				individual.setAlive(true);
				individual.setDeath("NA");
				individual.setChild("NA");
				individual.setSpouse("NA");
				individuals.add(individual);
			} else if (list.get(0).equals("0") && !list.get(list.size() - 1).equals("INDI")) {
				for (int j = i + 1; j < information.size(); j++) {
					List<String> temp = sliceInformation(information.get(j));
					if (temp.get(0).equals("0") && temp.get(temp.size() - 1).equals("INDI")) {
						i = j - 1;
						break;
					}
				}
			} else if (list.get(1).equals("NAME")) {
				String name = "";
				for (int j = 2; j < list.size(); j++) {
					name += list.get(j) + " ";
				}
				name = name.trim();
				individuals.get(individuals.size() - 1).setName(name);;
			} else if (list.get(1).equals("SEX")) {
				individuals.get(individuals.size() - 1).setGender(list.get(2));
			} else if (list.get(1).equals("BIRT")) {
				List<String> temp = sliceInformation(information.get(++i));
				String year = temp.get(4);
				String month = convertMonth(temp.get(3));
				String day = temp.get(2);
				if (day.length() == 1) {
					day = "0" + day;
				}
				day = "-" + day;
				Individual individual = individuals.get(individuals.size() - 1);
				individual.setBrithday(year + month + day);
				individual.setAge(2018 - Integer.parseInt(year));
			} else if (list.get(1).equals("DEAT")) {
				List<String> temp = sliceInformation(information.get(++i));
				String year = temp.get(4);
				String month = convertMonth(temp.get(3));
				String day = temp.get(2);
				if (day.length() == 1) {
					day = "0" + day;
				}
				day = "-" + day;
				Individual individual = individuals.get(individuals.size() - 1);
				individual.setDeath((year + month + day));
				individual.setAlive(false);
			} else if (list.get(1).equals("FAMC")) {
				String familyId = list.get(2).replaceAll("@", "");
				Individual individual = individuals.get(individuals.size() - 1);
				individual.setChild(familyId);
			} else if (list.get(1).equals("FAMS")) {
				String familyId = list.get(2).replaceAll("@", "");
				Individual individual = individuals.get(individuals.size() - 1);
				individual.setSpouse(familyId);
			}
		}
	}
	
	public void writeFamily() {
		for (int i = 0; i < information.size(); i++) {
			List<String> list = sliceInformation(information.get(i));
			if (list.get(0).equals("0") && list.size() >= 3 && list.get(2).equals("FAM")) {
				Family family = new Family();
				family.setId(list.get(1).replaceAll("@", ""));
				family.setDivorced("NA");
				families.add(family);
			} else if (list.get(1).equals("MARR")) {
				List<String> temp = sliceInformation(information.get(++i));
				String year = temp.get(4);
				String month = convertMonth(temp.get(3));
				String day = temp.get(2);
				if (day.length() == 1) {
					day = "0" + day;
				}
				day = "-" + day;
				families.get(families.size() - 1).setMarried(year + month + day);
			} else if (list.get(1).equals("DIV")) {
				List<String> temp = sliceInformation(information.get(++i));
				String year = temp.get(4);
				String month = convertMonth(temp.get(3));
				String day = temp.get(2);
				if (day.length() == 1) {
					day = "0" + day;
				}
				day = "-" + day;
				families.get(families.size() - 1).setDivorced(year + month );
			} else if (list.get(1).equals("HUSB")) {
				String husbandId = list.get(2);
				husbandId = husbandId.replaceAll("@", "");
				Individual individual = map.get(husbandId);
				Family family = families.get(families.size() - 1);
				family.setHusbandId(husbandId);
				family.setHusbandName(individual.getName());
			} else if (list.get(1).equals("WIFE")) {
				String wifeId = list.get(2);
				wifeId = wifeId.replaceAll("@", "");
				Individual individual = map.get(wifeId);
				Family family = families.get(families.size() - 1);
				family.setWifeId(wifeId);
				family.setWifeName(individual.getName());
			} else if (list.get(1).equals("CHIL")) {
				String childernId = list.get(2);
				childernId = childernId.replaceAll("@", "");
				Family family = families.get(families.size() - 1);
				List<String> temp = family.getChildren();
				if (temp == null) {
					temp = new ArrayList<String>();
				}
				temp.add(childernId);
				family.setChildren(temp);
			}
		}
	}
	
	public String convertMonth(String month) {
		String res = null;
		switch (month) {
		case "JAN": res = "-01";
					break;
		case "FEB": res = "-02";
					break;
		case "MAR": res = "-03";
					break;
		case "APR": res = "-04";
					break;
		case "MAY": res = "-05";
					break;
		case "JUN": res = "-06";
					break;
		case "JUL": res = "-07";
					break;
		case "AUG": res = "-08";
					break;
		case "SEP": res = "-09";
					break;
		case "OCT": res = "-10";
					break;
		case "NOV": res = "-11";
					break;
		case "DEC": res = "-12";
					break;
		}
		return res;
	}
	
	public static void main(String[] args) {
		GedcomReader gr = new GedcomReader();
		gr.readFile("Project01_JingtingZhang.ged");
		gr.writeIndividual();
		gr.writeFamily();
		Collections.sort(gr.individuals, new SortIndividual());
		Collections.sort(gr.families, new SortFamily());
		

		System.out.println("Individuals");
		System.out.println("+-----+--------------------+--------+-----------+-----+-------+------------+-----------+-----------+");
		System.out.println("| ID  | Name               | Gender | Birthday  | Age | Alive | Death      | Child     | Spouse    |");
		System.out.println("+-----+--------------------+--------+-----------+-----+-------+------------+-----------+-----------+");
		for (Individual i : gr.individuals) {
			System.out.printf("|%-5s|%-20s|%-8s|%-11s|%-5d|%-7b|%-12s|%-11s|%-11s|%n", i.getId(), i.getName(), i.getGender(), i.getBrithday(), i.getAge(), i.isAlive(), i.getDeath(), "{'" + i.getChild() + "'}", "{'" + i.getSpouse() + "'}");
		}
		System.out.println("+-----+--------------------+--------+-----------+-----+-------+------------+-----------+-----------+");
		System.out.println("Families");
		System.out.println("+-----+------------+------------+------------+--------------------+-----------+--------------------+------------------+");
		System.out.println("| ID  | Married    | Divorced   | Husband ID | Husband Name       | Wife ID   | Wife Name          | Childern         |");
		for (Family f : gr.families) {
			System.out.printf("|%-5s|%-12s|%-12s|%-12s|%-20s|%-11s|%-20s|%-18s|%n", f.getId(), f.getMarried(), f.getDivorced(), f.getHusbandId(), f.getHusbandName(), f.getWifeId(), f.getWifeName(), "{" + f.getChildren().toString() + "}");
		}
		System.out.println("+-----+------------+------------+------------+--------------------+-----------+--------------------+------------------+");
	}
}
