package hska.iwi.eShopMaster.model.database.dataobjects;

public class UserRegistration {
	
		private String username;
		private String firstname;
		private String lastname;
		private String password1;
		private String password2;

		public UserRegistration(String username, String firstname, String lastname, String password1,
				String password2) {
			super();
			this.username = username;
			this.firstname = firstname;
			this.lastname = lastname;
			this.password1 = password1;
			this.password2 = password2;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

		public String getPassword1() {
			return password1;
		}

		public void setPassword1(String password1) {
			this.password1 = password1;
		}

		public String getPassword2() {
			return password2;
		}

		public void setPassword2(String password2) {
			this.password2 = password2;
		}

	}