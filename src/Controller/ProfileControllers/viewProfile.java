package Controller.ProfileControllers;

import Entity.Profile;

public class viewProfile {
    public Profile viewOneProfile(int profile_id){
        Profile profileEntity = new Profile();
        Profile profile  = profileEntity.viewOneProfile(profile_id);
        return profile;
    }
}
