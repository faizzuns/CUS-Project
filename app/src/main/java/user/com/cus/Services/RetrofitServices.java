package user.com.cus.Services;

import user.com.cus.Services.ApiHelper.EditProfileServie;
import user.com.cus.Services.ApiHelper.FaqService;
import user.com.cus.Services.ApiHelper.HistoryService;
import user.com.cus.Services.ApiHelper.ListItemService;
import user.com.cus.Services.ApiHelper.ListPlaceService;
import user.com.cus.Services.ApiHelper.LoginService;
import user.com.cus.Services.ApiHelper.PaymentService;
import user.com.cus.Services.ApiHelper.RateService;
import user.com.cus.Services.ApiHelper.RegisterService;
import user.com.cus.Utils.RetrofitClientUtils;

/**
 * Created by User on 24/01/2018.
 */

public class RetrofitServices {

    public static LoginService sendLoginRequest(){
        return RetrofitClientUtils.client().create(LoginService.class);
    }

    public static RegisterService sendRegisterRequest(){
        return RetrofitClientUtils.client().create(RegisterService.class);
    }

    public static ListPlaceService sendListPlaceRequest(){
        return RetrofitClientUtils.client().create(ListPlaceService.class);
    }

    public static EditProfileServie sendEditProfileRequest(){
        return RetrofitClientUtils.client().create(EditProfileServie.class);
    }

    public static FaqService sendFaqRequest(){
        return RetrofitClientUtils.client().create(FaqService.class);
    }

    public static ListItemService sendListItemRequest(){
        return RetrofitClientUtils.client().create(ListItemService.class);
    }

    public static PaymentService sendPaymentRequest(){
        return RetrofitClientUtils.client().create(PaymentService.class);
    }

    public static HistoryService sendHistoryRequest(){
        return RetrofitClientUtils.client().create(HistoryService.class);
    }

    public static RateService sendRateRequest(){
        return RetrofitClientUtils.client().create(RateService.class);
    }
}
