package hska.iwi.eShopMaster.model.businessLogic.manager;

import hska.iwi.eShopMaster.model.database.dataobjects.User;


public interface UserManager {
    
    public void registerUser(String username, String name, String lastname, String password);
    
    public User getUser(String username, String password);
    
    public boolean deleteUserById(int id);
    
//    public Role getRoleByLevel(int level);
    
    public boolean doesUserAlreadyExist(String username);
}
