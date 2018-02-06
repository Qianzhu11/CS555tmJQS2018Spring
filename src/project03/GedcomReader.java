package project03;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GedcomReader {
	List<String> information;
	List<Individual> individuals;
	List<Family> families;
	
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
			// TODO Auto-generated catch block
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
	
	public void writeFile() {
		for (int i = 0; i < information.size(); i++) {
			List<String> list = sliceInformation(information.get(i));
			for (int j = 0; j < list.size(); j++) {
				if (list.get(0) == "0" && list.get(list.size() - 1) == "INDI") {
					Individual individual = new Individual();
					String id = list.get(1);
					individual.setId(id.substring(1, id.length() - 1));
				} 
			}
		}
	}
	
	public static void main(String[] args) {
		GedcomReader gr = new GedcomReader();
		
	}

}
