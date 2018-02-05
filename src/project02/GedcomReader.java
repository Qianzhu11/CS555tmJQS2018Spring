package project02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GedcomReader {
	
	List<String> list;
	List<String> res;
	List<String> legalTag = Arrays.asList(new String[]{"0INDI", "1SEX", "1BIRT", "1DEAT", "1FAMC", "1FAMS", "0FAM", "1MARR", "1HUSB",
			"1WIFE", "1CHIL", "1DIV", "0HEAD", "0TRLR", "0NOTE", "1NAME", "2DATE"});
	Set<String> set = new HashSet<>(legalTag);
	
	public void readFile(String file) {
		list = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
				list.add(s);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeFile() {
		res = new ArrayList<String>();
		String string1 = null;
		String string = null;
		for (String s : list) {
			boolean contains = false;
			res.add("--> " + s);
			List<String> l = sliceString(s);
			if (l.get(1).equals("INDI") || l.get(1).equals("FAM")) {
				contains = false;
			} else {
				if (l.size() > 1) {
					String s1 = l.get(0) + l.get(1);
					if (set.contains(s1)) {
						contains = true;
						string = l.get(1);
					}
				} 
				if (l.size() > 2) {
					String s1 = l.get(0) + l.get(2);
					if (set.contains(s1)) {
						contains = true;
						string = l.get(2);
					}
				}
			}
			if (contains) {
				if (sliceString(s).size() >= 3 && string.equals("FAM") || string.equals("INDI")) {
					List<String> temp = sliceString(s);
					string1 = "<-- " + temp.get(0) + "|" + temp.get(2) + "|" + "Y" + "|" + temp.get(1);
					res.add(string1);
				} else {
					List<String> temp = sliceString(s);
					string1 = "<-- ";
					for (int i = 0; i < temp.size(); i++) {
						if (i < 2) {
							if (i == 1) {
								string1 += temp.get(i) + "|Y|";
							} else {
								string1 += temp.get(i) + "|";
							}
						} else {
							string1 += temp.get(i) + " ";
						}
					}
					res.add(string1);
				}
			} else {
				List<String> temp = sliceString(s);
				string1 = "<-- ";
				for (int i = 0; i < temp.size(); i++) {
					if (i < 2) {
						if (i == 1) {
							string1 += temp.get(i) + "|N|";
						} else {
							string1 += temp.get(i) + "|";
						}
					} else {
						string1 += temp.get(i) + " ";
					}
				}
				res.add(string1);
			}
		}
	}
	
	public List<String> sliceString(String s) {
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
	
	public static void main(String[] args) {
		GedcomReader gr = new GedcomReader();
		gr.readFile("My-Family-23-Jan-2018-889.ged");
		gr.writeFile();
		for (String s : gr.res) {
			System.out.println(s);
		}
	}
}
