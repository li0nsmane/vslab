package hska.webshop.userservice.model;

public enum UserLevel {
	ADMIN(0),
	CUSTOMER(1);
	
	private int levelId;
	
	private UserLevel(int levelId) {
		this.levelId = levelId;
	}
	
	public int getLevelId() {
		return levelId;
	}
}
