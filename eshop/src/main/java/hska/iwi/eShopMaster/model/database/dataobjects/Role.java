package hska.iwi.eShopMaster.model.database.dataobjects;


public class Role {

	private int role_id;
	private String typ;
	
	private int level;

	public Role() {
	}

	public Role(String typ, int level) {
		this.typ = typ;
		this.level = level;
	}

	public int getRoleId() {
		return this.role_id;
	}

	public void setRoleId(int id) {
		this.role_id = id;
	}

	public String getTyp() {
		return this.typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
