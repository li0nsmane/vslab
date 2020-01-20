package hska.iwi.eShopMaster.model.database.dataobjects;


public class CreationCategory  {

	private int id;
	private String name;

	public CreationCategory() {
	}

	public CreationCategory(String name) {
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
