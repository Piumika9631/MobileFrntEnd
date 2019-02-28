package Other;

public class Constants {
    public static final String BASE_URL = "https://parkheresl.herokuapp.com";//"https://projectl2.herokuapp.com";


    public static final String USER_FNAME = "user_fname";
    public static final String USER_LNAME = "user_lname";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_NIC = "user_nic";
    public static final String USER_MOBILE = "user_mobile";
    public static final String USER_REGCODE = "user_regcode";
    public static final String USER_REGISTERED = "user_registered";
    public static final String USER_PREF = "user_preferences";

    //payhere
    public static final int PAYHERE_REQUEST = 110111;


    public static final String[] VEHICLE_TYPES = {"Car","Van", "Bus","Bicycle","Lorry","Threewheel"};
    public static String GET_TYPE_NUMBER(String type){
        for(int i=0;i<5;i++){
            if(VEHICLE_TYPES[i].equals(type))
                return Integer.toString(i+1);
        }
        return "";
    }
}
