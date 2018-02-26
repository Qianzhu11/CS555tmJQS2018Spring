package Project04;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.core.CombinableMatcher;
import org.junit.Test;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

public class SprintTest {
	
	private GedcomReader gr;
	
	public SprintTest() {
		gr = new GedcomReader();
		gr.readFile("testFile.ged");
		gr.writeIndividual();
		gr.writeFamily();
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
			if (!family.getDivorced().equals("NA")) {
				assertTrue("Error: FAMILY: US06: " + family.getId() + ": Divorced " + family.getDivorced() + " after " + who + " (" + temp.getId() + ") death on " + temp.getDeath(), gr.compareDate(family.getDivorced(), temp.getDeath()));				
			}
		}
	}
	
	@Test
	public void testUserStory07() {
		
	}
}
