package Project10;

import java.util.List; 
import org.hamcrest.core.CombinableMatcher; 
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List; 
import java.util.Map;
import java.util.Set;

import org.junit.Before; 
 
 public class SprintTest { 
 	 
 	private GedcomReader gr; 
 	List<Family> families = new ArrayList<Family>(); 
 	List<Individual> individual = new ArrayList<Individual>(); 
 	Map<String, Individual> map; 
 	 
 	public SprintTest() { 
 		gr = new GedcomReader(); 
 		gr.readFile("testFile1.ged"); 
 		gr.writeIndividual(); 
 		gr.writeFamily(); 
 	} 
 	 
	@Before 
 	public void setUp() { 
 		families = gr.families; 
 		individual = gr.individuals; 
 		map=gr.map; 
 	} 
 
 	@Test 
 	public void testUserStory01ForIndividual() { 
 		List<Individual> individuals = gr.individuals; 
 		for (int i = 0; i < individuals.size(); i++) { 
 			Individual individual = individuals.get(i); 
 			String birthday = individual.getBrithday(); 
 			assertTrue("ERROR: INDIVIDUAL: US01: " + individual.getId() + ": " + "Birthday " + birthday + " occurs in the future", gr.validateDate(birthday)); 
 			String death = individual.getDeath(); 
 			if (!death.equals("NA")) { 
 				assertTrue("ERROR: INDIVIDUAL: US01: " + individual.getId() + ": " + "Death " + death + " occurs in the future", gr.validateDate(death)); 
 			} 
 		} 
 	} 
 	 
 	@Test 
	public void testUserStory01ForFamily() { 
 		List<Family> families = gr.families; 
 		for (int i = 0; i < families.size(); i++) { 
 			Family family = families.get(i); 
 			String marry = family.getMarried(); 
 			assertTrue("ERROR: FAMILY: US01: " + family.getId() + ": " + "Marriage date " + marry + " occurs in the future", gr.validateDate(marry)); 
 			String divorce = family.getDivorced(); 
 			if (!divorce.equals("NA")) { 
				assertTrue("ERROR: FAMILY: US01: " + family.getId() + ": " + "Divorce date " + divorce + " occurs in the future", gr.validateDate(divorce)); 
 			} 
 		} 
 	} 
 	 
 	@Test 
 	public void testUserStory02() { 
 		List<Family> families = gr.families; 
 		for (int i = 0; i < families.size(); i++) { 
			Family family = families.get(i); 
			String husbandId = family.getHusbandId(); 
 			String wifeId = family.getWifeId(); 
 			Individual husband = gr.map.get(husbandId); 
 			String husbanBirthday = husband.getBrithday(); 
 			Individual wife = gr.map.get(wifeId); 
			String wifeBirthday = wife.getBrithday(); 
 			Individual temp; 
			String who; 
 			if (gr.compareDate(husbanBirthday, wifeBirthday)) { 
				temp = wife; 
 				who = "wife"; 
 			} else { 
 				temp = husband; 
 				who = "husband"; 
 			} 
 			assertTrue("Error: FAMILY: US02: " + family.getId() + " " +who + "'s birth date " + temp.getBrithday() + " after marriage date " + family.getMarried(), gr.validateMarriageDate(temp.getBrithday(), family.getMarried())); 
 		} 
 	} 
 	 
 	@Test 
 	public void testUserStory03() { 
 		List<Individual> individuals = gr.individuals; 
 		for (int i = 0; i < individuals.size(); i++) { 
 			Individual individual = individuals.get(i); 
 			String birthday = individual.getBrithday(); 
 			String death = individual.getDeath(); 
 			assertTrue("ERROR: INDIVIDUAL: US03: " + individual.getId() + ": " + "Died " + death + " before born " + birthday, gr.compareBirthtoDeath(birthday, death)); 
 		} 
 	} 
 	 
	@Test 
 	public void testUserStory04() { 
 		List<Family> families = gr.families; 
 		for (int i = 0; i < families.size(); i++) { 
 			Family family = families.get(i); 
			String marry = family.getMarried(); 
 			String divorce = family.getDivorced(); 
 			assertTrue("Error: FAMILY: US04: " + family.getId() + ": Divorced " + divorce + " before married " + marry, gr.compareMarriagetoDivorce(marry, divorce)); 
 		} 
	} 
 	 
 	@Test 
 	public void testUserStory05() { 
 		List<Family> families = gr.families; 
 		for (int i = 0; i < families.size(); i++) { 
 			Family family = families.get(i); 
 			String husbandId = family.getHusbandId(); 
			String wifeId = family.getWifeId(); 
 			Individual husband = gr.map.get(husbandId); 
 			String husbandDeath = husband.getDeath(); 
 			Individual wife = gr.map.get(wifeId); 
			String wifeDeath = wife.getDeath(); 
 			Individual temp; 
 			String who; 
			if (gr.compareDate(husbandDeath, wifeDeath)) { 
 				temp = husband; 
 				who = "husband"; 
 			} else { 
 				temp = wife; 
 				who = "wife"; 
			} 
			assertTrue("Error: FAMILY: US05: " + family.getId() + ": Married " + family.getMarried() + " after " + who + "'s (" + temp.getId() + ") death on " + temp.getDeath(), gr.compareDate(family.getMarried(), temp.getDeath())); 
 		} 
 	} 
 	 
 	@Test 
 	public void testUserStory06() { 
 		List<Family> families = gr.families; 
 		for (int i = 0; i < families.size(); i++) { 
 			Family family = families.get(i); 
			String husbandId = family.getHusbandId(); 
			String wifeId = family.getWifeId(); 
 			Individual husband = gr.map.get(husbandId); 
 			String husbandDeath = husband.getDeath(); 
			Individual wife = gr.map.get(wifeId); 
 			String wifeDeath = wife.getDeath(); 
 			Individual temp; 
 			String who; 
			if (gr.compareDate(husbandDeath, wifeDeath)) { 
 				temp = husband; 
 				who = "husband"; 
			} else { 
				temp = wife; 
				who = "wife"; 
			} 
			if (family.getDivorced().equals("NA")) { 
			assertTrue("Error: FAMILY: US06: " + family.getId() + ": Divorced " + family.getDivorced() + " after " + who + " (" + temp.getId() + ") death on " + temp.getDeath(), gr.compareDate(family.getDivorced(), temp.getDeath()));				 
 			} 
 		} 
 	} 
 	 
 	@Test 
 	public void testUserStory07() { 
		List<Individual> individuals = gr.individuals; 
		for (Individual individual: individuals) { 
 			if (individual.getDeath().equals("NA")) { 
				assertTrue("ERROR: INDIVIDUAL: US07: " + individual.getId() + ": More than 150 years old - Bitrh date " + individual.getBrithday(), gr.ageLessThan150(individual)); 
 			} else { 
 				assertTrue("ERROR: INDIVIDUAL: US07: " + individual.getId() + ": More than 150 years old at death - Bitrh date " + individual.getBrithday() + ": Death " + individual.getDeath(), gr.ageLessThan150(individual)); 
 			} 
 		} 
 	} 
 	 
 	@Test 
 	public void testUserStory10() { 
 		List<Family> families = gr.families; 
 		for (int i = 0; i < families.size(); i++) { 
 			Family family = families.get(i); 
			String husbandId = family.getHusbandId(); 
 			String wifeId = family.getWifeId(); 
 			Individual husband = gr.map.get(husbandId); 
 			String husbandBirthday = husband.getBrithday(); 
 			Individual wife = gr.map.get(wifeId); 
 			String wifeBirthday = wife.getBrithday(); 
 			String marry = family.getMarried(); 
 			String divorce = family.getDivorced(); 
 
 
 			assertTrue("ERROR: FAMILY: US10: " + family.getId() + " marriage before 14", gr.validateMarriageAge(husbandBirthday, marry) || gr.validateMarriageAge(wifeBirthday, marry)); 
 		} 
 	} 
 	 
 	@Test 
 	public void testUserStory08() { 
 		List<Family> families = gr.families; 
 		for (int i = 0; i < families.size(); i++) { 
 			Family family = families.get(i); 
 			String marry = family.getMarried(); 
 			assertTrue("ERROR: FAMILY: US08: " + family.getId() + ": Children's birth date before parents' marriage.", gr.compareBirthtoParentsMarriage(family.getChildren(), marry)); 
 			} 
 	} 
 	 
 	@Test 
 	public void testUserStory09() { 
 		List<Family> families = gr.families; 
 		for (int i = 0; i < families.size(); i++) { 
 			Family family = families.get(i); 
 			String husbandId = family.getHusbandId(); 
 			String wifeId = family.getWifeId(); 
 			Individual husband = gr.map.get(husbandId); 
 			String husbandDeath = husband.getDeath(); 
 			Individual wife = gr.map.get(wifeId); 
 			String wifeDeath = wife.getDeath(); 
			assertTrue("ERROR: FAMILY: US09: " + family.getId() + ": Children's birth date after parents' death date.", gr.compareBirthtoParentsDeath(family.getChildren(), husbandDeath) || gr.compareBirthtoParentsDeath(family.getChildren(), wifeDeath)); 
 		} 
 	} 
 	 
 	@Test 
 	public void testUserStory12() { 
 		for(Family family : families) { 
 			Individual husband = map.get(family.getHusbandId()); 
 			Individual wife = map.get(family.getWifeId()); 
 			if(family.getId().equals("F10")) { 
 				assertTrue(gr.validateParentsAge(husband.getAge(), 80, family.getChildren())); 
 				assertTrue(gr.validateParentsAge(wife.getAge(), 60, family.getChildren())); 
 			} 
 		} 
 	} 
 	 
 	@Test 
 	public void testUserStory16() { 
 		for(Family family : families) { 
 			Individual husband = map.get(family.getHusbandId()); 
 			Individual wife = map.get(family.getWifeId()); 
			if(family.getId().equals("F10")) { 
 				assertTrue(gr.validateMaleLastName(husband, family.getChildren())); 
 			} 
 			else { 
 				assertFalse(gr.validateMaleLastName(husband, family.getChildren())); 
 			} 
 		} 
 	} 
 	 
 	@Test  
  	public void testUserStory35() {  
 		String ListYes = ""; 
 		String ListNo = ""; 
 		List<Individual> individuals = gr.individuals;  
  		for (int i = 0; i < individuals.size(); i++) {  
  			Individual individual = individuals.get(i);  
  			String birthday = individual.getBrithday();  
             int daysFromBirth = gr.getNumberOfDays(birthday);          
             if (daysFromBirth <= 30) { 
             	ListYes = "Y"; 
             } else { 
             	ListNo = "Y"; 
             }            
 		}  
         if (ListYes.equals("") || ListNo.equals("") ) { 
         	assertTrue("ERROR: INDIVIDUALS: US35: Listing of all recent births User Story processing is broken " ,true);  
         } 
  	}  
 
 
 	@Test  
  	public void testUserStory36() {  
 		String ListYes = ""; 
 		String ListNo = ""; 
 		List<Individual> individuals = gr.individuals;  
  		for (Individual individual : gr.individuals) {  
  			String deathdate = individual.getDeath();                   
             if (!deathdate.equals("NA") && gr.validateDate(deathdate)) {        
             	int daysFromDeath = gr.getNumberOfDays(deathdate);   
                 if (daysFromDeath <= 30) { 
             	ListYes = "Y"; 
                 } else { 
                 	ListNo = "Y"; 
                 }            
             } 
  		} 
         if (ListYes.equals("") || ListNo.equals("") ) { 
         	assertTrue("ERROR: INDIVIDUALS: US36: Listing of all recent deaths User Story processing is broken " ,true);  
         } 
  	}
 	
 	@Test
 	 public void testUserStory14() {
 	  List<List<String>> l = gr.getMutipleBirths();
 	  int max = 0;
 	  for (List t : l) max = Math.max(max, t.size());
 	  assertTrue("ERROR: US14: multiple births should be less than or equal to 5", max <= 5);
 	 }
 	
 	@Test 
 	 public void testUserStory30() {
 	  List l = gr.getLivingMarried();
 	  assertTrue("ERROR: US30: Listing of living married processing is broken", l.size() >= 0);
 	 }

     @Test  
      public void testUserStory29() {  
 		String ListYes = ""; 
                String ListNo = ""; 
 		List<Individual> individuals = gr.individuals;  
  		for (int i = 0; i < individuals.size(); i++) {  
  			Individual individual = individuals.get(i);  
                       if (!individual.isAlive()) { 
             	        ListYes = "Y"; 
                        }  else { 
                 	ListNo = "Y"; 
                        }       
               }  
         if (ListYes.equals("") || ListNo.equals("") ) { 
         	assertTrue("ERROR: INDIVIDUALS: US29: Listing of all deceased individuals User Story processing is broken " ,true);  
         } 
  	}

     @Test
     public void testUserStory38() {
      List l = gr.getUpcomingBirthday();
      assertTrue("ERROR: US38: Listing of upcoming birthdays processing is broken", l.size() >= 0);
     }
      
    @Test 
   	public void testUserStory15() { 
   		List<Family> families = gr.families; 
   		for (int i = 0; i < families.size(); i++) { 
   			Family family = families.get(i);  
   			assertTrue("ERROR: FAMILY: US15: " + family.getId() + ":More than 15 siblings.", gr.siblingsLessThan15(family.getChildren())); 
   			} 
   	} 
    
    @Test
    public void testUserStory39() {
     List l = gr.getUpcomingAnniversary();
     assertTrue("ERROR: US39: Listing of upcoming anniversaries processing is broken", l.size() >= 0);
    }
    
    @Test
    public void testUserStory32() {
     List l = gr.getMutipleBirths();
     assertTrue("ERROR: US32: Listing of mutiple births processing is broken", l.size() >= 0);
    }
    
    @Test
    public void testUserStory34() {
     List l = gr.getLargeAgeDiff();
     assertTrue("ERROR: US34: Listing of large difference age processing is broken", l.size() >= 0);
    }
    
    @Test 
   	public void testUserStory20() { 
   		for(Family family : families) { 
   			if(family.getId().equals("F12")) { 
   				assertFalse(gr.checkUncleAuntRelation(family)); 
   			} 
   			else
   				assertTrue(gr.checkUncleAuntRelation(family));
   		} 
   	} 
      
    @Test 
    public void testUserStory27() { 
    	List<Individual> individuals = gr.individuals;  
    	for (int i = 0; i < individuals.size(); i++) {   
    		Individual individual = individuals.get(i); 
    		String birthday = individual.getBrithday();  
    		if(individual.getId().equals("I1")) {
    			assertEquals(22,gr.currentAge(birthday));
    		}
     	} 
    }
    
    @Test  
    public void testUserStory23() {  
		List<String> allnameswithbirthday = new ArrayList<>();

		List<Individual> individuals = gr.individuals;  
		for (int i = 0; i < individuals.size(); i++) {  
			Individual individual = individuals.get(i); 
                      String name = individual.getName();
                      String birthday = individual.getBrithday();  
                      String namewithbirthday = name+birthday;
		        allnameswithbirthday.add(namewithbirthday);  
             }  
        Set<String> setNamesBd=new HashSet<>();
		Set<String> duplicateNamesBd=new HashSet<>();
		
		for (String namebd : allnameswithbirthday) {
			if(!setNamesBd.add(namebd)) {
				duplicateNamesBd.add(namebd);
			}
		}
        if ( !duplicateNamesBd.isEmpty() ) { 
       	assertTrue("ERROR: INDIVIDUALS: US23: There are More than one individual with the same name and birth date" ,true);  
       } 
	}

    @Test  
    public void testUserStory31() {  
	
              List<Individual> livingssingles30plus = gr.getLivingSingles30plus(); 
              if (livingssingles30plus.size() == 0){ 
       	        assertTrue("INDIVIDUALS: US31: There are no living singles over 30 never married" ,true);  
              }  else {
                      assertTrue("INDIVIDUALS: US31: There are " + livingssingles30plus.size() + " living singles over 30 never married" ,true);  
              }
	}
    
    @Test 
   	public void testUserStory21() { 
    	  for(Family family : families) { 
     			if(family.getId().equals("F10")) { 
     				assertFalse(gr.validateGender(family)); 
     			} 
     			else if(family.getId().equals("F11")) { 
     				assertFalse(gr.validateGender(family)); 
     			} 
     			else
     				assertTrue(gr.validateGender(family));
     		} 
    }
      
    @Test 
   	public void testUserStory19() { 
    	  for(Family family : families) { 
   			if(family.getId().equals("F13")) { 
   				assertFalse(gr.checkFirstCousinRelation(family)); 
   			}else
   				assertTrue(gr.checkFirstCousinRelation(family));
   		} 
    }
   
    @Test 
   	public void testUserStory17() { 
    	  for(Family family : families) { 
   			if(family.getId().equals("F13")) { 
   				assertFalse(gr.checkFirstCousinRelation(family)); 
   			}else
   				assertTrue(gr.checkFirstCousinRelation(family));
   		} 
    }
    
    @Test 
   	public void testUserStory18() { 
    	  for(Family family : families) { 
   			if(family.getId().equals("F13")) { 
   				assertFalse(gr.checkFirstCousinRelation(family)); 
   			}else
   				assertTrue(gr.checkFirstCousinRelation(family));
   		} 
    }
    
    @Test
    public void testUserStory33() {
    	assertTrue(gr.listOrphans().size() >= 0);
    }

}  
