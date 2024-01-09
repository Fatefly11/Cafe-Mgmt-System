package Controller.ProfileControllers;

import Entity.Profile;

public class deleteProfile {
    public String deleteOneProfile(int profile_id){
        Profile profileEntity = new Profile();
        String result = profileEntity.deleteOneProfile(profile_id);
        System.out.println(result);
        return result;
    }
}
