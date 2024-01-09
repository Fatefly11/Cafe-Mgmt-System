package Controller.ProfileControllers;

import Entity.Profile;

import java.util.List;

public class viewAllProfiles {
    public List<Profile> viewAllProfiles(){
        Profile profileEntity = new Profile();
        List<Profile> profiles;
        profiles = profileEntity.viewAllProfiles();
        return profiles;
    }
}
