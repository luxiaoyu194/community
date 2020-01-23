package com.luyu.community.controller;

import com.luyu.community.dto.AccessTokenDTO;
import com.luyu.community.dto.GithubUser;
import com.luyu.community.mapper.UserMapper;
import com.luyu.community.model.User;
import com.luyu.community.provide.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

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

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/rallback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request){
        AccessTokenDTO   accessTokenDTO    = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setState(state);
        String token = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(token);
        if(githubUser!=null){
            request.getSession().setAttribute("user",githubUser);
            User user = new User();
            user.setName(githubUser.getName());
            user.setGmtCreate(System.currentTimeMillis());
            user.setToken(UUID.randomUUID().toString());
            user.setAccountId(""+githubUser.getId());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            return "redirect:/";
            //登录成功，写session和cookie
        }else{
            return "redirect:/";
            //登录失败
        }
    }
}
