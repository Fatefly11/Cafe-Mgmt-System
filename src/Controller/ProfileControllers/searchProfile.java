package Controller.ProfileControllers;

import Entity.Profile;

import java.util.ArrayList;
import java.util.List;

public class searchProfile {
    public List<Profile> searchProfileBySubString(String substring){
        Profile profileEntity = new Profile();
        List<Profile> profiles;
        profiles = profileEntity.searchProfileBySubString(substring);
        return profiles;
    }
}
