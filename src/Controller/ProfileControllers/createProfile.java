package Controller.ProfileControllers;

import Entity.Profile;

public class createProfile {
    public String createNewProfile(String profile_name, String profile_desc){
        Profile profileEntity = new Profile();
        String result = profileEntity.createNewProfile(profile_name, profile_desc);
        System.out.println(result);
        return result;
    }
}
