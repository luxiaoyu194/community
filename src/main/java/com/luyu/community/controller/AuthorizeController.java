package com.luyu.community.controller;

import com.luyu.community.dto.AccessTokenDTO;
import com.luyu.community.dto.GithubUser;
import com.luyu.community.provide.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Value("${github.client.secret}")
    private String clientSecret;

    @GetMapping("/rallback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state){
        AccessTokenDTO   accessTokenDTO    = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setState(state);
        String token = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(token);
        return "index";
    }
}
