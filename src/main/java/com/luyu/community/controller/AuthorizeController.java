package com.luyu.community.controller;

import com.luyu.community.dto.AccessTokenDTO;
import com.luyu.community.dto.GithubUser;
import com.luyu.community.provide.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/rallback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state){
        AccessTokenDTO   accessTokenDTO    = new AccessTokenDTO();
        accessTokenDTO.setClient_id("427f855d1394563cb658");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8080/rallback");
        accessTokenDTO.setClient_secret("c2e87d0e2bf0a71bfcfcadf3c460555ea9945644");
        accessTokenDTO.setState(state);
        String token = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(token);
        return "index";
    }
}
