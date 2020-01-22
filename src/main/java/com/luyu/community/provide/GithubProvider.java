package com.luyu.community.provide;

import com.alibaba.fastjson.JSON;
import com.luyu.community.dto.AccessTokenDTO;
import com.luyu.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.imageio.IIOException;
import java.io.IOException;

@Component
public class GithubProvider {


    public String getAccessToken(AccessTokenDTO accessTokenDTO){

        MediaType mediaType
                = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        String json = JSON.toJSONString(accessTokenDTO);
        RequestBody body = RequestBody.create(mediaType,json);
         Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String str = response.body().string();
                str = str.split("=")[1].split("&")[0];
                return str;
            }catch (Exception e){
                return "";
            }
    }

    public GithubUser getUser(String token){
        OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.github.com/user?access_token="+token)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String json = response.body().string();
                GithubUser githubUser = JSON.parseObject(json,GithubUser.class);
                return githubUser;
            }catch (IOException E){
                return null;
            }
    }
}
