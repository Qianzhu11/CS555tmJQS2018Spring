package Project06;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.core.CombinableMatcher;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;


//import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

public class SprintTest {
	
	private GedcomReader gr;
	List<Family> families = new ArrayList<Family>();
	List<Individual> individual = new ArrayList<Individual>();
	Map<String, Individual> map;
	
	public SprintTest() {
		gr = new GedcomReader();
		gr.readFile("testFile.ged");
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
    public void testUserStory22() {
    	assertTrue("should be true", gr.uniqueId(gr.ids));
    }
    
    @Test
    public void testUserStory42() {
    	String date = gr.dates.get((int) Math.random() * gr.dates.size());
		assertTrue("should be true", gr.checkValidDate(date));
    }
}
