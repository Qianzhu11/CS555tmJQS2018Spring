package Project10;

import java.io.BufferedReader; 
import java.io.FileReader; 
import java.io.IOException; 
import java.text.ParseException; 
import java.text.SimpleDateFormat; 
import java.util.ArrayList; 
import java.util.Collections; 
import java.util.Date; 
import java.util.HashMap; 
import java.util.HashSet; 
import java.util.List; 
import java.util.Map; 
import java.util.Set; 
import java.time.LocalDate; 
import java.time.temporal.ChronoUnit;  
import java.time.LocalDateTime;    
import java.time.Period;
 
 
 public class GedcomReader { 
 	List<String> information; 
 	List<Individual> individuals; 
 	List<Family> families; 
 	Map<String, Individual> map; 
 	List<String> ids; 
 	List<String> dates; 
 	 
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
 		dates = new ArrayList<>(); 
 		ids = new ArrayList<>(); 
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
 				individual.setAge(individual.getAge() - 2018 + Integer.parseInt(year)); 
 			} else if (list.get(1).equals("FAMC")) { 
 				String familyId = list.get(2).replaceAll("@", ""); 
 				Individual individual = individuals.get(individuals.size() - 1); 
 				individual.setChild(familyId); 
 			} else if (list.get(1).equals("FAMS")) { 
 				String familyId = list.get(2).replaceAll("@", ""); 
				Individual individual = individuals.get(individuals.size() - 1); 
 				individual.setSpouse(familyId); 
 			} else if (list.size() >= 2 && list.get(1).equals("DATE")) { 
 				String date = list.get(4) + " " + list.get(3) + " " + list.get(2); 
 				dates.add(date); 
 			} else if (list.size() >= 3 && list.get(2).equals("INDI")) { 
 				ids.add(list.get(1).replaceAll("@", "")); 
			} else if (list.size() >= 3 && list.get(2).equals("FAM")) { 
 				ids.add(list.get(1).replaceAll("@", "")); 
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
 				families.get(families.size() - 1).setDivorced(year + month + day); 
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
 	 
 	//Sprint 1 
 	//US01 
 	public boolean validateDate(String inputDate) { 
 		try { 
 			if (!inputDate.equalsIgnoreCase("NA")) { 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
 	            Date date1 = sdf.parse(inputDate); 
	            if(!inputDate.equals(sdf.format(date1))) 
	            	return false; 
 	            if (date1.after(new Date())) { 
 	            	return false; 
 	            } 
 			} 
 		} catch(ParseException e) { 
 			System.out.println("Exception in parsing date" + inputDate); 
 			return false; 
 		} 
 		return true; 
 	} 
 	 
 	//US02 
 	public boolean validateMarriageDate(String birthDate,String marriageDate) { 
 		try { 
 			if (!marriageDate.equalsIgnoreCase("NA")) { 
 				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	            Date birDate = sdf.parse(birthDate); 
	            Date marrDate = sdf.parse(marriageDate); 
 	            if (birDate.after(marrDate)) { 
 	            	return false; 
 	            } 
 			} 
 		} catch(ParseException e) { 
 			System.out.println("Exception in parsing date"); 
 			return false; 
 		} 
 		return true; 
 	} 
 	 
 	//US03 
 	public boolean compareBirthtoDeath(String date1, String date2) { 
 		date1 = date1.replaceAll("-", ""); 
 		date2 = date2.replaceAll("-", ""); 
 		if (date1 == "NA") { 
 			return false; 
 		} else if (date2 == "NA") { 
 			return true; 
 		} else if (Integer.parseInt(date1) < Integer.parseInt(date2)) { 
 			return true; 
 		} 
 		return false; 
 	} 
 	 
 	//US04 
 	public boolean compareMarriagetoDivorce(String date1, String date2) { 
 		date1 = date1.replaceAll("-", ""); 
 		date2 = date2.replaceAll("-", ""); 
 		if (date1 == "NA") { 
 			return false; 
 		} else if (date2 == "NA") { 
 			return true; 
 		} else if (Integer.parseInt(date1) < Integer.parseInt(date2)) { 
 			return true; 
 		}  
 		return false; 
 	} 
 	 
 	//US05&US06 
 	public boolean compareDate(String date1, String date2) { 
 		date1 = date1.replaceAll("-", ""); 
 		date2 = date2.replaceAll("-", ""); 
 		if (date2.equals("NA")) { 
 			return true; 
 		} 
 		if (date1.equals("NA") && !date2.equals("NA")) { 
 			return false; 
 		} 
 		if (Integer.parseInt(date1) <= Integer.parseInt(date2)) { 
 			return true; 
 		} 
 		return false; 
 	} 
 	 
 	//US07 
 	public boolean ageLessThan150(Individual individual) { 
 		return individual.getAge() < 150; 
 	} 
 	 
 	//US10 
 	public boolean validateMarriageAge(String date1, String date2) { 
 		date1 = date1.replaceAll("-", ""); 
 		date2 = date2.replaceAll("-", ""); 
 		if (date1.equals("NA")) { 
 			return false; 
 		} 
 		if (!date1.equals("NA") && date2.equals("NA")) { 
 			return true; 
		} 
 		if ((Integer.parseInt(date1) + 140000) <= Integer.parseInt(date2)) { 
 			return true; 
 		} 
 		return false; 
 	} 
 	 
 	//Sprint 2 
 	//US08 
 	public boolean compareBirthtoParentsMarriage(List<String> childrenIds, String date){ 
 		for(String childID: childrenIds){ 
 			Individual child = map.get(childID); 
 			String childBirthday = child.getBrithday().replaceAll("-", ""); 
 			date = date.replaceAll("-", ""); 
 			if (date.equals("NA")){ 
 				return false; 
 			} 
 			if(Integer.parseInt(childBirthday) <= Integer.parseInt(date)){ 
 				return false; 
 			} 
 		}			 
 		return true; 
 	} 
 	 
 	//US09 
 	public boolean compareBirthtoParentsDeath(List<String> childrenIds, String date){ 
 		for(String childID : childrenIds){ 
 			Individual child = map.get(childID); 
 			String childBirthday = child.getBrithday().replaceAll("-", ""); 
 			date = date.replaceAll("-", ""); 
 			if (date.equals("NA")){ 
 				return true; 
 			} 
			if(Integer.parseInt(childBirthday) <= Integer.parseInt(date)){ 
 				return true; 
			} 
 		}			 
 		return false; 
 	} 
 
 	// US12 
 	public boolean validateParentsAge(int age, int compareTo, List<String> childIds) { 
 		for (String childID : childIds) { 
			Individual child = map.get(childID); 
 			if (age > child.getAge() + compareTo ) { 
 				return true; 
			} 
 		} 
		return false; 
 	} 
 	 
 	//US16 
	public boolean validateMaleLastName(Individual husband, List<String> childrenId) { 
 		String lastName = husband.getName().split(" ")[1]; 
 		for (String childId : childrenId) { 
 			Individual child = map.get(childId); 
 			if(child.getGender().equalsIgnoreCase("M") && !child.getName().split(" ")[1].equalsIgnoreCase(lastName)) 
 				return true; 
 		}		 
 		return false; 
 	} 
 	 
	//US35
    public List<Individual> getRecentBirth() {
   	 List<Individual> res = new ArrayList<Individual>();
   	 for (Individual i : individuals) {
   		 int birth = Integer.parseInt(i.getBrithday().replaceAll("-", ""));
   		 if (20180327 - birth < 30 && 20180327 - birth > 0)
   			 res.add(i);
   	 }
   	 return res;
    }
    
	//US36 
     public int getNumberOfDays(String date1) {  
 	     String year = date1.substring(0,4);   
             String month = date1.substring(5,7);  
             String day = date1.substring(8); 
             LocalDate today = LocalDate.now(); 
             LocalDate date2 = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)); 
             long noofdays = ChronoUnit.DAYS.between(date2, today); 
             int days = (int) noofdays; 
 	        return days;  
 	}
     
    //US30
 	public List<Individual> getLivingMarried() {
		List<Individual> res = new ArrayList<Individual>();
		for (Individual i : individuals)
			if (i.isAlive() && !i.getSpouse().equals("NA"))
				res.add(i);
		return res;
	}
     
    //Sprint 3
    //US15 
  	public boolean siblingsLessThan15(List<String> childrenIds) { 
  		int siblingscount = 0;
 		for (String childId : childrenIds) { 
 			Individual child = map.get(childId);
 			siblingscount++;
 		}
		if(siblingscount <= 15){
 			return true;
 		}
 		return false;  
  	} 
  	  
  	//US38
    public List<Individual> getUpcomingBirthday() {
   	 List<Individual> res = new ArrayList<Individual>();
   	 for (Individual i : individuals)
   		 if (i.isAlive()) {
   			 int birth = Integer.parseInt(i.getBrithday().replaceAll("-", "").substring(4,  8));
       		 if (birth > 327 && birth < 427) res.add(i);	 
   		 }
   	 return res;
    }
    
  	//US39
    public List<List<String>> getUpcomingAnniversary() {
    	List<List<String>> res = new ArrayList<List<String>>();
    	for (Family f : families) {
 			if(f.getDivorced().equals("NA")){
 	 			Individual h = map.get(f.getHusbandId());
 	 			Individual w = map.get(f.getWifeId());
 	 			int marry = Integer.parseInt(f.getMarried().replaceAll("-", "").substring(4,  8));
 	 			if (marry > 409 && marry < 509){
 	 				List<String> l = new ArrayList<String>();
 	 				l.add(f.getId());
 	 				l.add(h.getName());
 	 				l.add(w.getName());
 	 				l.add(f.getMarried());
 	 				res.add(l);
 	 			}
 			}
    	}
    	return res;	
    } 
     
     //US14 & US32
     public List<List<String>> getMutipleBirths() {
 		List<List<String>> res = new ArrayList<List<String>>();
 		for (Family f : families) {
 			Map<String, List<String>> _m = new HashMap<String, List<String>>();
 			List<String> children = f.getChildren();
 			for (String id : children) {
 				String birth = map.get(id).getBrithday();
 				if (!_m.containsKey(birth))
 					_m.put(birth, new ArrayList<String>());
 				_m.get(birth).add(id);
 			}
 			for (String date : _m.keySet())
 				if (_m.get(date).size() >= 2)
 					res.add(_m.get(date));
 		}
 		return res;
 	}
     
     //US34
     public List<List<String>> getLargeAgeDiff() {
 		List<List<String>> res = new ArrayList<List<String>>();
 		for (Family f : families) {
 			Individual h = map.get(f.getHusbandId());
 			Individual w = map.get(f.getWifeId());
 			int mYear = Integer.parseInt(f.getMarried().substring(0, 4));
 			int hYear = mYear - Integer.parseInt(h.getBrithday().substring(0, 4));
 			int wYear = mYear - Integer.parseInt(w.getBrithday().substring(0, 4));
 			if (hYear > 2 * wYear || wYear > 2 * hYear) {
 				List<String> l = new ArrayList<String>();
 				l.add(f.getId());
 				l.add(h.getName());
 				l.add(w.getName());
 				l.add("" + hYear);
 				l.add("" + wYear);
 				res.add(l);
 			}
 		}
 		return res;
 	}
   
     //US20 
     public boolean checkUncleAuntRelation(Family fam) {
     	
     	String husbandFamID =map.get(fam.getHusbandId()).getChild();
     	String wifeFamId = map.get(fam.getWifeId()).getChild();
     	for (Family f : families) {
     		if(f.getId().equals(husbandFamID)) {
     			List<String> childIds = f.getChildren();
     			for(String childId: childIds) {
     				if(map.get(childId).getSpouse().equals(wifeFamId) && !childId.equals(fam.getHusbandId()))
     					return false;
     			}
     		}
     		else if(f.getId().equals(wifeFamId)) {
     			List<String> childIds = f.getChildren();
     			for(String childId: childIds) {
     				if(map.get(childId).getChild().equals(husbandFamID) && !childId.equals(fam.getWifeId()))
     					return false;
     			}
     		}
     	}
     	return true;
     }
     
     //US27
     public int currentAge(String dob) {
    	 //int age = 0;
    	  LocalDate birthDate = LocalDate.parse(dob);
    	  LocalDate now = LocalDate.now();
    	  return Period.between(birthDate, now).getYears();
    	 
     }
     
     //US29 
     public List<Individual> getDeceased() {
     	 List<Individual> res = new ArrayList<Individual>();
     	 for (Individual i : individuals)
     		 if (!i.isAlive())
     			 res.add(i);
     	 return res;
      }
     
     //Sprint4
     //US31
     public List<Individual> getLivingSingles30plus() { 
  		List<Individual> res = new ArrayList<Individual>(); 
  		for (Individual i : individuals) 
  			if (i.isAlive() && i.getSpouse().equals("NA") && i.getAge() > 30 ) 
  				res.add(i); 
 		return res; 
  	 }
     
     //US21
     public boolean validateGender(Family fam) {
    	 String husbandId = fam.getHusbandId();
    	 String wifeID = fam.getWifeId();
    	 if(!"M".equals(map.get(husbandId).getGender()))
    		 return false;
    	 else if (!"F".equals(map.get(wifeID).getGender()))
    		 return false;
    	 return true;
     }
     
     //US19
     public boolean checkFirstCousinRelation(Family fam) {
     	
     	String husbandFamID =map.get(fam.getHusbandId()).getChild();
     	String wifeFamId = map.get(fam.getWifeId()).getChild();
     	String husbandFatherFamID= "",husbandMotherFamID = "";
     	String wifeFatherFamID= "",wifeMotherFamID = "";
     	if (!husbandFamID.equalsIgnoreCase("NA") && !wifeFamId.equalsIgnoreCase("NA")) {
     	for (Family f : families) {
     		if(f.getId().equals(husbandFamID)) {
     			husbandFatherFamID = map.get(f.getHusbandId()).getChild();
     			husbandMotherFamID = map.get(f.getWifeId()).getChild();
     		}
     		else if(f.getId().equals(wifeFamId)) {
     			wifeFatherFamID = map.get(f.getHusbandId()).getChild();
     			wifeMotherFamID = map.get(f.getWifeId()).getChild();
     		}
     	}
     	
     	if (!husbandFatherFamID.equalsIgnoreCase("NA") && (husbandFatherFamID.equals(wifeFatherFamID) || husbandFatherFamID.equals(wifeMotherFamID)))
     		return false;
     	if (!husbandFatherFamID.equalsIgnoreCase("NA") && (husbandMotherFamID.equals(wifeFatherFamID) || husbandMotherFamID.equals(wifeMotherFamID)))
     		return false;
     	}
     	return true;
     }
     
     //US17 
     public boolean noMarriagetoDescendant(Family fam) {
    	String husbandFamID =map.get(fam.getHusbandId()).getChild();
      	String wifeFamId = map.get(fam.getWifeId()).getChild();
      	String husbandFatherFamID= "",husbandMotherFamID = "";
      	String wifeFatherFamID= "",wifeMotherFamID = "";
      	if (!husbandFamID.equalsIgnoreCase("NA") && !wifeFamId.equalsIgnoreCase("NA")) {
      		for (Family f : families) {
      			if(f.getId().equals(husbandFamID)) {
      				husbandMotherFamID = map.get(f.getWifeId()).getChild();
      			}
      			else if(f.getId().equals(wifeFamId)) {
      				wifeFatherFamID = map.get(f.getHusbandId()).getChild();
      			}
      		}
      		if (husbandFamID.equals(wifeFatherFamID) || wifeFamId.equals(husbandMotherFamID)){
      			return false;
      		}
      	}
      	return true;
     }
     
     //US18
     public boolean noSiblingMarriage(Family fam) {
     	
     	String husbandFamID =map.get(fam.getHusbandId()).getChild();
     	String wifeFamId = map.get(fam.getWifeId()).getChild();
     	String husbandFatherFamID= "",husbandMotherFamID = "";
     	String wifeFatherFamID= "",wifeMotherFamID = "";
     	if (!husbandFamID.equalsIgnoreCase("NA") && !wifeFamId.equalsIgnoreCase("NA")) {
     		for (Family f : families) {
     			if(f.getId().equals(husbandFamID)) {
     				husbandFatherFamID = map.get(f.getHusbandId()).getChild();
     				husbandMotherFamID = map.get(f.getWifeId()).getChild();
     			}
     			else if(f.getId().equals(wifeFamId)) {
     				wifeFatherFamID = map.get(f.getHusbandId()).getChild();
     				wifeMotherFamID = map.get(f.getWifeId()).getChild();
     			}
     		}
     		if (!husbandFatherFamID.equalsIgnoreCase("NA") && (husbandFatherFamID.equals(wifeFatherFamID) && husbandMotherFamID.equals(wifeMotherFamID))){
     			return false;
     		}
     	}
     	return true;
     }
     
     public boolean checkValidDate(String date) { 
  		String[] strings = date.split(" "); 
  		int year = Integer.parseInt(strings[0]); 
  		String month = strings[1]; 
  		int day = Integer.parseInt(strings[2]); 
  		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) { 
  			if (month.equals("FEB") && day > 0 && day <= 29) return true;  
  		} else { 
  			if (month.equals("JAN") || month.equals("MAR") || month.equals("MAY") || month.equals("JUL") 
  				|| month.equals("AUG") || month.equals("OCT") || month.equals("DEC")) { 
  				if (day > 0 && day <= 31) return true; 
  			} else if (month.equals("APR") || month.equals("JUN") || month.equals("SEP") || month.equals("NOV")) { 
  				if (day > 0 && day <= 30) return true; 
  			} else if (month.equals("FEB") && day > 0 && day <= 28) return true; 
  		} 
  		return false; 
     }
     
     public void sortSiblings() {
    	 for (Family f : families)
    		 Collections.sort(f.getChildren(), (a, b) -> map.get(a).getAge() - map.get(b).getAge());
     }
     
     public List<List<String>> listOrphans() {
    	 List<List<String>> res = new ArrayList<List<String>>();
    	 for (Family f : families) 
    		 if (!map.get(f.getHusbandId()).isAlive() && !map.get(f.getWifeId()).isAlive())
    			 res.add(f.getChildren());
    	 return res;
     }

      public static void main(String[] args) { 
     	 GedcomReader gr = new GedcomReader(); 
     	 gr.readFile("testFile2.ged"); 
     	 gr.writeIndividual(); 
     	 gr.writeFamily(); 
     	 Collections.sort(gr.individuals, new SortIndividual()); 
     	 Collections.sort(gr.families, new SortFamily()); 
     	 gr.sortSiblings();
  
  
  		System.out.println("Individuals"); 
  		System.out.println("+-----+--------------------+--------+-----------+-----+-------+------------+-----------+-----------+------------+"); 
  		System.out.println("| ID  | Name               | Gender | Birthday  | Age | Alive | Death      | Child     | Spouse    | Current Age|"); 
  		System.out.println("+-----+--------------------+--------+-----------+-----+-------+------------+-----------+-----------+------------+"); 
 		for (Individual i : gr.individuals) { 
 			int age = 0;
 			if(i.isAlive()) {
 				age = gr.currentAge(i.getBrithday());
 			}
  			System.out.printf("|%-5s|%-20s|%-8s|%-11s|%-5d|%-7b|%-12s|%-11s|%-11s|%-12s|%n", i.getId(), i.getName(), i.getGender(), i.getBrithday(), i.getAge(), i.isAlive(), i.getDeath(), "{'" + i.getChild() + "'}", "{'" + i.getSpouse() + "'}",age); 
  		} 
  		System.out.println("+-----+--------------------+--------+-----------+-----+-------+------------+-----------+-----------+------------+");
  		System.out.println("Families"); 
  		System.out.println("+-----+------------+------------+------------+--------------------+-----------+--------------------+------------------+"); 
  		System.out.println("| ID  | Married    | Divorced   | Husband ID | Husband Name       | Wife ID   | Wife Name          | Childern         |"); 
  		for (Family f : gr.families) { 
  			System.out.printf("|%-5s|%-12s|%-12s|%-12s|%-20s|%-11s|%-20s|%-18s|%n", f.getId(), f.getMarried(), f.getDivorced(), f.getHusbandId(), f.getHusbandName(), f.getWifeId(), f.getWifeName(), "{" + f.getChildren().toString() + "}"); 
  		} 
  		System.out.println("+-----+------------+------------+------------+--------------------+-----------+--------------------+------------------+"); 
  		System.out.println(); 
  	
        int recentdeathscount = 0;
        List<String> allnameswithbirthday = new ArrayList<>();
          
  		for (Individual individual : gr.individuals) { 
  			String birthday = individual.getBrithday(); 
  			 
 			if (!gr.validateDate(birthday)) { 
  				System.out.println("ERROR: INDIVIDUAL: US01: " + individual.getId() + ": " + "Birthday " + birthday + " occurs in the future"); 
  			} 
  			String death = individual.getDeath(); 
  			if (!death.equals("NA") && !gr.validateDate(death)) { 
  				System.out.println("ERROR: INDIVIDUAL: US01: " + individual.getId() + ": " + "Birthday " + birthday + " occurs in the future"); 
  			} 
  			 
  			if(!gr.compareBirthtoDeath(birthday, death)){ 
  				System.out.println("ERROR: INDIVIDUAL: US03: " + individual.getId() + ": " + "Died " + death + " before born " + birthday); 
  			} 
  			if (individual.getDeath().equals("NA")) { 
  				if(!gr.ageLessThan150(individual)){ 
  					System.out.println("ERROR: INDIVIDUAL: US07: " + individual.getId() + ": More than 150 years old - Bitrh date " + individual.getBrithday()); 
  				} 
  			} else { 
  				if(!gr.ageLessThan150(individual)){ 
  					System.out.println("ERROR: INDIVIDUAL: US07: " + individual.getId() + ": More than 150 years old at death - Bitrh date " + individual.getBrithday() + ": Death " + individual.getDeath()); 
  				} 
  			} 
              String deathdate = individual.getDeath();                   
              if (!deathdate.equals("NA") && gr.validateDate(deathdate)) {        
              	int daysFromDeath = gr.getNumberOfDays(deathdate);   
                 if (daysFromDeath <= 30) { 
                     recentdeathscount++; 
                     System.out.println("\nLIST: US36: All people who died in the last 30 days");  
                     System.out.println("+-----+--------------------+--------+-----------+");  
                     System.out.println("| ID  | Name               | Gender | Death     |");  
                     System.out.println("+-----+--------------------+--------+-----------+");  
                     System.out.printf("|%-5s|%-20s|%-8s|%-11s|%n\n", individual.getId(), individual.getName(), individual.getGender(), individual.getDeath() );  
                 }  
              }
              String name = individual.getName(); 
              String namewithbirthday = name+birthday;
              allnameswithbirthday.add(namewithbirthday);
  		 }   
         if (recentdeathscount == 0) {                 
         	System.out.println("LIST: US36: None died in the last 30 days");   
         }
         
         //US23 begin
         Set<String> setNamesBd=new HashSet<>();
         Set<String> duplicateNamesBd=new HashSet<>();
	
         for (String namebd : allnameswithbirthday) {
        	 if(!setNamesBd.add(namebd)) {
        		 duplicateNamesBd.add(namebd);
        	 }
         }
         if ( !duplicateNamesBd.isEmpty() ) { 
             System.out.println("ERROR: INDIVIDUAL: US23: There are More than one individual with the same name and birth date");   
        }
        //US23 end
  		
         List<Family> families = gr.families; 
  		for (int i = 0; i < families.size(); i++) { 
  			Family family = families.get(i); 
  			String husbandId = family.getHusbandId(); 
  			String wifeId = family.getWifeId(); 
  			Individual husband = gr.map.get(husbandId); 
  			String husbandBirthday = husband.getBrithday(); 
  			String husbandDeath = husband.getDeath(); 
  			Individual wife = gr.map.get(wifeId); 
  			String wifeBirthday = wife.getBrithday(); 
  			String wifeDeath = wife.getDeath(); 
  			Individual temp; 
  			String who; 
  			if (gr.compareDate(husbandBirthday, wifeBirthday)) { 
 				temp = wife; 
  				who = "wife"; 
 			} else { 
 				temp = husband; 
  				who = "husband"; 
  			} 
  			 
  			if(!gr.validateMarriageDate(temp.getBrithday(), family.getMarried())){ 
  				System.out.println("ERROR: FAMILY: US02: " + family.getId() + ": " +who + "'s birth date " + temp.getBrithday() + " after marriage date " + family.getMarried()); 
  			} 
  			 
  			String marry = family.getMarried(); 
  			String divorce = family.getDivorced(); 
  			if(!gr.compareMarriagetoDivorce(marry, divorce)){ 
  				System.out.println("ERROR: FAMILY: US04: " + family.getId() + ": Divorced " + divorce + " before married " + marry); 
  			} 
  			 
  			if(!gr.compareDate(family.getMarried(), temp.getDeath())){ 
 				System.out.println("ERROR: FAMILY: US05: " + family.getId() + ": Married " + family.getMarried() + " after " + who + "'s (" + temp.getId() + ") death on " + temp.getDeath()); 
  			} 
  			 
  			if (gr.compareDate(husbandDeath, wifeDeath)) { 
  				temp = husband; 
 				who = "husband"; 
  			} else { 
  				temp = wife; 
  				who = "wife"; 
  			} 
  			if (!family.getDivorced().equals("NA")) { 
  				if(gr.compareDate(family.getDivorced(), temp.getDeath())){ 
  					System.out.println("ERROR: FAMILY: US06: " + family.getId() + ": Divorced " + family.getDivorced() + " after " + who + " (" + temp.getId() + ") death on " + temp.getDeath());				 
  				} 
  			} 
  			if(!gr.validateMarriageAge(husbandBirthday, marry) || !gr.validateMarriageAge(wifeBirthday, marry)){ 
  				System.out.println("ERROR: FAMILY: US10: " + family.getId() + ": marriage before 14"); 
  			} 
  			if(!gr.compareBirthtoParentsMarriage(family.getChildren(), marry)){ 
  				System.out.println("ERROR: FAMILY: US08: " + family.getId() + ": Children's birth date before parents' marriage:" + family.getMarried() + " ."); 
  			} 
  			if(!gr.compareBirthtoParentsDeath(family.getChildren(), husbandDeath) || !gr.compareBirthtoParentsDeath(family.getChildren(), wifeDeath)){ 
 				System.out.println("ERROR: FAMILY: US09: " + family.getId() 
 				+ ": Children's birth date after parents' death date"); 
  			} 
  			if ((husband.isAlive() && gr.validateParentsAge(husband.getAge(), 80 , family.getChildren())) || 
  					(wife.isAlive() && gr.validateParentsAge(wife.getAge(), 60 , family.getChildren()))	) { 
  				System.out.println("ERROR: FAMILY: US12: " + family.getId() 
  						+ ": Father age is greater than 80 or Mother age is greater than 60 than their children"); 
 			} 
 			if (gr.validateMaleLastName(husband, family.getChildren())	) { 
 				System.out.println("ERROR: FAMILY: US16: " + family.getId() 
  						+ ": Last name of all male member of family is not same"); 
             } 
 			if(!gr.siblingsLessThan15(family.getChildren())){
 				System.out.println("ERROR: FAMILY: US15: " + family.getId() + ": More than 15 siblings");
 			}
             
             if (!gr.checkUncleAuntRelation(family)	) { 
 				System.out.println("ERROR: FAMILY: US20: " + family.getId() 
  						+ ": Aunts and uncles should not marry their nieces or nephews"); 
              }
             if (!gr.validateGender(family)	) { 
 				System.out.println("ERROR: FAMILY: US21: " + family.getId() 
  						+ ": Husband in family should be male and wife in family should be female"); 
              }
 			 if (!gr.checkFirstCousinRelation(family)) { 
 				System.out.println("ERROR: FAMILY: US19: " + family.getId() 
  						+ ": First cousins should not marry one another"); 
              }
 			 if (!gr.noSiblingMarriage(family)) { 
 				System.out.println("ERROR: FAMILY: US18: " + family.getId() 
  						+ ": Siblings should not marry one another"); 
              }
 			 if (!gr.noMarriagetoDescendant(family)) { 
 				System.out.println("ERROR: FAMILY: US17: " + family.getId() 
  						+ ": Parents should not marry any of their descendants"); 
              }
  		}
  		System.out.println("\nLIST: US39: All anniversaries in the coming 30 days");
  		List<List<String>> upcomingAnniversaries = gr.getUpcomingAnniversary();
  		if(upcomingAnniversaries.size() == 0) System.out.println("There is no upcoming anniversaries");
  		else {
            System.out.println("\nLIST: US39: All anniversaries in the coming 30 days");  
            System.out.println("+-----+--------------------+--------------------+-----------+");  
            System.out.println("| ID  | HusbandName        |WifeName            | Marrydate |");  
            System.out.println("+-----+--------------------+--------------------+-----------+");
            for (List<String> cur : upcomingAnniversaries)
            	System.out.printf("|%-5s|%-20s|%-20s|%-11s|%n", cur.get(0), cur.get(1), cur.get(2), cur.get(3));
  		}
  		
  		System.out.println("\nLIST: US32: List multiple births");
 		List<List<String>> multipleBirths = gr.getMutipleBirths();
 		if (multipleBirths.size() == 0) System.out.println("There is no multiple births");
 		else {
 			System.out.println("+-----+--------------------+-----------+"); 
 			System.out.println("| ID  | Name               | Birthday  |"); 
 			for (List<String> cur : multipleBirths)
 				for (String id : cur)
 					System.out.printf("|%-5s|%-20s|%-11s|%n", id, gr.map.get(id).getName(), gr.map.get(id).getBrithday());  
 		}
  		System.out.println("\nLIST: US34: List large age differences");
 		List<List<String>> largeAge = gr.getLargeAgeDiff();
 		if (largeAge.size() == 0) System.out.println("There are no such couples");
 		else {
             System.out.println("+-----+--------------------+-------------------+------------------------+--------------------+");  
             System.out.println("| ID  | HusbandName        |WifeName           |Husband age when married|Wife age when married");  
             for (List<String> t : largeAge) 
             	System.out.printf("|%-5s|%-20s|%-19s|%-24s|%-20s|%n", t.get(0), t.get(1), t.get(2), t.get(3), t.get(4));
     	}
 		
 		int maxBirth = 0;
 		String bDate = "";
 		for (List<String> temp : multipleBirths)
 			if (temp.size() > maxBirth) {
 				maxBirth = temp.size();
 				bDate = gr.map.get(temp.get(0)).getBrithday();
 			}
         if (maxBirth >= 6) System.out.println("\nERROR: FAMILY: US14: Multiple births: " + maxBirth + " people were born in " + bDate);
 		
 		List<Individual> livings = gr.getLivingMarried();
 		System.out.println("\nLIST: US30: List all living married");
 		if (livings.size() == 0) System.out.println("There are no living married");
 		else {
             System.out.println("+-----+--------------------+");  
             System.out.println("| ID  | Name               |");  
             for (Individual in : livings) 
             	System.out.printf("|%-5s|%-20s|%n", in.getId(), in.getName());
 		}
 		
 		System.out.println("\nLIST: US29: List all deceased people");
 		List<Individual> deceased = gr.getDeceased();
 		if (deceased.size() == 0) System.out.println("There is no deceased people");
 		else {
 			System.out.println("+-----+-------------------+---------+---------------+");
 			System.out.println("| ID  | Name              | Gender  | Deathday      |");
 			System.out.println("+-----+-------------------+---------+---------------+");
 			for (Individual deaed : deceased)
 				System.out.printf("|%-5s|%-19s|%-9s|%-15s|%n", deaed.getId(), deaed.getName(), deaed.getGender(), deaed.getDeath());
 		}
 		
 		System.out.println("\nLIST: US38: List all upcoming birthday");
 		List<Individual> upcomingBirthday = gr.getUpcomingBirthday();
 		if (upcomingBirthday.size() == 0) System.out.println("There is no upcoming birthday");
 		else {
 			System.out.println("+-----+-------------------+---------+---------------+");
 			System.out.println("| ID  | Name              | Gender  | Deathday      |");
 			System.out.println("+-----+-------------------+---------+---------------+");
 			for (Individual birth : upcomingBirthday)
 				System.out.printf("|%-5s|%-19s|%-9s|%-15s|%n", birth.getId(), birth.getName(), birth.getGender(), birth.getBrithday());
 		}
 		
 		System.out.println("\nLIST: US35: List all recent births");
 		List<Individual> recentBirths = gr.getRecentBirth();
 		if (recentBirths.size() == 0) System.out.println("There is no recent births");
 		else {
 			System.out.println("+-----+-------------------+---------+---------------+");
 			System.out.println("| ID  | Name              | Gender  | Birthday      |");
 			System.out.println("+-----+-------------------+---------+---------------+");
 			for (Individual recentB : recentBirths)
 				System.out.printf("|%-5s|%-19s|%-9s|%-15s|%n", recentB.getId(), recentB.getName(), recentB.getGender(), recentB.getBrithday());

 		}
 		
  	} 
 } 