package com.zczczy.by.xfy.rest;


import com.zczczy.by.xfy.model.ApplyAccount;
import com.zczczy.by.xfy.model.ApplyAgent;
import com.zczczy.by.xfy.model.BaseModel;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.CityProvince;

import com.zczczy.by.xfy.model.Equip;
import com.zczczy.by.xfy.model.FireInfo;
import com.zczczy.by.xfy.model.Insp;
import com.zczczy.by.xfy.model.PagerResult;
import com.zczczy.by.xfy.model.PushMsg;
import com.zczczy.by.xfy.model.UpdateApp;
import com.zczczy.by.xfy.model.UserLogin;

import org.androidannotations.annotations.rest.Delete;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Put;
import org.androidannotations.annotations.rest.RequiresAuthentication;
import org.androidannotations.annotations.rest.RequiresCookie;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.annotations.rest.SetsCookie;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.androidannotations.api.rest.RestClientHeaders;
import org.androidannotations.api.rest.RestClientRootUrl;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * Created by Leo on 2015/3/9.
 */

@Rest(rootUrl = "http://182.92.85.199:7890/api", requestFactory = MyRequestFactory.class, interceptors = { MyInterceptor.class },
        converters = {StringHttpMessageConverter.class,	MappingJackson2HttpMessageConverter.class,FormHttpMessageConverter.class, ByteArrayHttpMessageConverter.class })
public interface MyRestClient extends RestClientRootUrl, RestClientSupport, RestClientHeaders, RestClientErrorHandling {

    @Post("/Post/{id}")
    @RequiresHeader(value = {"Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE, "Content-Disposition", "filename", "charset"})
    String uploadAvatar(int id, MultiValueMap<String, Object> formData);

    @Post("/testPostEntity/{id}")
    String testPostEntity(int id, BaseModel bm);


    @Delete("/deleteTest/{id}")
    @RequiresAuthentication
    BaseModel deleteTest(int id);


    @Get("/Login/{id}")
    @SetsCookie("JSESSIONID")
    BaseModel login(int id);

    /**
     * 必须传入一个JSESSIONID
     * 也就是说，必须在登录的情况下才可以
     *
     * @param page
     * @param rows
     * @return
     */
    @Post("/getVideoInfoList/{page}/{rows}")
    @RequiresCookie("JSESSIONID")
    String getVideoInfoList(int page, int rows);

    @Put("/Put")
    String putTest(BaseModel bm);


    //void setHeader(String value,String va);

    //用户登录验证
    @Get("/Passport/Login?username={username}&password={password}")
    BaseModelJson<UserLogin>Login(String username, String password);
    //火警故障
    @Get("/Business/GetBusinessList?company={company}&pageIndex={pageIndex}&pageSize={pageSize}&btype={btype}&type={type}")
    @RequiresHeader("Token")
    BaseModelJson<PagerResult<FireInfo>>GetBusinessList(String company, int pageIndex,int pageSize,String btype,String type);
    //巡检查岗
    @Get("/Business/GetACheckList?company={company}&pageIndex={pageIndex}&pageSize={pageSize}&type={type}")
    @RequiresHeader("Token")
    BaseModelJson<PagerResult<Insp>>GetACheckList(String company, int pageIndex,int pageSize,String type);
    //城市选择
    @Get("/Apply/GetProvinceAndCity")
    BaseModelJson<List<CityProvince>>GetProvinceAndCity();
    //我的设备
    @Get("/Business/GetDeviceInfo")
    @RequiresHeader("Token")
    BaseModelJson<Equip> GetDeviceInfo();
    //法人申请
    @Post("/Apply/ApplyAccount")

    BaseModelJson<String>ApplyAccount(ApplyAccount aa);

    //法人申请
    @Post("/Apply/ApplyAgent")
    BaseModelJson<String>ApplyAgent(ApplyAgent aa);
    //修改密码
    @Post("/Business/ChangePassword?username={username}&oldPassword={oldPassword}&newPassword={newPassword}")
    @RequiresHeader("Token")
    BaseModelJson<String>ChangePassword(String username,String oldPassword,String newPassword );

    //我的设备
    @Get("/Business/GetBusiness/{id}")
    @RequiresHeader("Token")
    BaseModelJson<FireInfo> GetBusiness(int id);

    //我的消息

    @Get("/Business/GetPushMessage?username={username}&pageIndex={pageIndex}&pageSize={pageSize}")
    @RequiresHeader("Token")
    BaseModelJson<PagerResult<PushMsg>>GetPushMessage(String username, int pageIndex,int pageSize);

    //获取更新
    @Get("/Passport/GetVersion/{id}")
    BaseModelJson<UpdateApp> GetVersion(int id);

}
