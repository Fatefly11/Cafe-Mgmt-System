package Controller.ProfileControllers;

import Entity.Profile;

public class updateProfile {
    public String updateOneProfile(int user_id, String profile_name, String profile_desc){
        Profile profileEntity = new Profile();
        String result = profileEntity.updateOneProfile(user_id, profile_name, profile_desc);
        System.out.println(result);
        return result;
    }
}
