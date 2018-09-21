package user.com.cus.Utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import user.com.cus.DataModel.Item.ItemSaved;
import user.com.cus.DataModel.User.FavouriteModel;
import user.com.cus.DataModel.User.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24/01/2018.
 */

public class Utils {
    public static final String sharedHeader = "8f679de7e1942ecbca6902d936926ff1";
    public static final String sharedJsonHeader = "application/json";
    public static final String version = "Version 1.5.0";
    public static final int RC_SIGN_IN = 2;
    public static final int PERMISSION_FOR_CALL_PHONE = 10;
    public static final int DETAIL_HISTORY = 11;

    public static UserModel sharedUser;

    public static final int PERMISSION_FOR_LOCATION = 1;

    public static void closeSoftKeyboard(Context context, View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
    
    public static void printProfileData(UserModel userModel, String TAG){
        StringBuilder favourite = new StringBuilder("[");
        Log.d(TAG, "printProfileData: "+userModel.getListFavourite().size());
        for (int i = 0; i<userModel.getListFavourite().size(); i++){
            favourite.append("(");
            favourite.append(String.valueOf(userModel.getListFavourite().get(i).getItemId()));
            favourite.append(", ");
            favourite.append(String.valueOf(userModel.getListFavourite().get(i).getCount()));
            favourite.append(")");
            if (i != userModel.getListFavourite().size()-1){
                favourite.append(", ");
            }
        }
        favourite.append("]");
        Log.d(TAG, "printProfileData: "+"\n"+
                        userModel.getId()+"\n"+
                        userModel.getName()+"\n"+
                        userModel.getEmail()+"\n"+
                        userModel.getPhone()+"\n"+
                        favourite);
    }

    public static String listFavouriteToString(List<FavouriteModel> list){
        String data = "";
        for (int i =0; i<list.size(); i++){
            data += String.valueOf(list.get(i).getItemId());
            if (i != list.size()-1){
                data += ",";
            }
        }
        data += " ";
        for (int i =0; i<list.size(); i++){
            data += String.valueOf(list.get(i).getCount());
            if (i != list.size()-1){
                data += ",";
            }
        }

        return data;
    }

    public static List<FavouriteModel> stringToListFavourite(String data){
        List<FavouriteModel> list = new ArrayList<>();
        String[] arrData = data.split(" ");
        if (arrData.length == 2){
            String[] arrDataFavourite = arrData[0].split(",");
            String[] arrDataCount = arrData[1].split(",");
            for (int i = 0; i<arrDataFavourite.length; i++) {
                list.add(new FavouriteModel(Integer.parseInt(arrDataFavourite[i]), Integer.parseInt(arrDataCount[i])));
            }
        }
        Log.d("cekStringToList", "stringToListFavourite: "+list.size());
        return list;
    }

    public static String convertToRupiahFormat(int price){
        String data = "Rp ";

        try {
            data += String.format("%,d", price);
            data += ",-";
            return data;
        }catch (NumberFormatException e){
            return null;
        }

    }

    public static void syncDataFavourite(){
        List<FavouriteModel> listFavourite = Utils.sharedUser.getListFavourite();

    }

    public static int checkIdItemSaved(int id){
        int cek = -1;
        for (int i = 0; i<Utils.sharedUser.getListFavourite().size(); i++){
            if (id == Utils.sharedUser.getListFavourite().get(i).getItemId()){
                cek = i;
                break;
            }
        }
        return cek;
    }

    public static String parseCreatedAt(String createdAt){
        String[] dummy = createdAt.split("-");
        String month;
        switch (Integer.parseInt(dummy[1])){
            case 1:
                month = "Jan, ";
                break;
            case 2:
                month = "Feb, ";
                break;
            case 3:
                month = "Mar, ";
                break;
            case 4:
                month = "Apr, ";
                break;
            case 5:
                month = "May, ";
                break;
            case 6:
                month = "Jun, ";
                break;
            case 7:
                month = "Jul, ";
                break;
            case 8:
                month = "Aug, ";
                break;
            case 9:
                month = "Sep, ";
                break;
            case 10:
                month = "Oct, ";
                break;
            case 11:
                month = "Nov, ";
                break;
            case 12:
                month = "Des, ";
                break;
            default:
                month = ", ";
                break;
        }

        String time = "";
        if (time.length() == 4){
            time = dummy[3];
        }else{
            String[] times = dummy[3].split(":");
            int hour = Integer.parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);
            if (hour < 10) time += "0"+times[0];
            else time += times[0];
            time += ":";
            if (minute < 10) time += "0"+times[1];
            else time += times[1];
        }

        return dummy[0] + " " + month + time;
    }

    public static void addDataFavourite(ItemSaved item){
        item.save();
    }

    public static void deleteDataFavourite(ItemSaved item){
        item.delete();
    }

}
