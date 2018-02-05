import java.util.List;

public class Family {
	private String id;
	private String married;
	private String divorced;
	private String husbandId;
	private String husbandName;
	private String wifeId;
	private String wifeName;
	private List<Individual> children;
	
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
	public List<Individual> getChildren() {
		return children;
	}
	public void setChildren(List<Individual> children) {
		this.children = children;
	}
}
