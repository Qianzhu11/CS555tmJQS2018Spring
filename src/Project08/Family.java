package Project08;
import java.util.Comparator;
import java.util.List;

public class Family {
	private String id;
	private String married;
	private String divorced;
	private String husbandId;
	private String husbandName;
	private String wifeId;
	private String wifeName;
	private List<String> children;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMarried() {
		return married;
	}
	public void setMarried(String married) {
		this.married = married;
	}
	public String getDivorced() {
		return divorced;
	}
	public void setDivorced(String divorced) {
		this.divorced = divorced;
	}
	public String getHusbandId() {
		return husbandId;
	}
	public void setHusbandId(String husbandId) {
		this.husbandId = husbandId;
	}
	public String getHusbandName() {
		return husbandName;
	}
	public void setHusbandName(String husbandName) {
		this.husbandName = husbandName;
	}
	public String getWifeId() {
		return wifeId;
	}
	public void setWifeId(String wifeId) {
		this.wifeId = wifeId;
	}
	public String getWifeName() {
		return wifeName;
	}
	public void setWifeName(String wifeName) {
		this.wifeName = wifeName;
	}
	public List<String> getChildren() {
		return children;
	}
	public void setChildren(List<String> children) {
		this.children = children;
	}
}

class SortFamily implements Comparator<Family> {

	@Override
	public int compare(Family o1, Family o2) {
		String s1 = o1.getId();
		String s2 = o2.getId();
		int i = Integer.parseInt(s1.substring(1));
		int j = Integer.parseInt(s2.substring(1));
		return i - j;
	}
	
}