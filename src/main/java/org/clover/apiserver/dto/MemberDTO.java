package org.clover.apiserver.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberDTO extends User {  //시큐리티가 사용하는 타입에 맞춰 DTO 생성

    private String email, pw, nickname;

    private Boolean social;

    private List<String> roleNames = new ArrayList<>();


    public MemberDTO(String email, String pw, String nickname, boolean social, List<String> roleNames) {

        super(
                email,
                pw,                           // 우리는 문자열로 권한을 받지만 시큐리티 쪽에서는 객체로 받아야함
                roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList()));

        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims() {

        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("email", email);
        dataMap.put("pw", pw);
        dataMap.put("nickname", nickname);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);

        return dataMap;
    }




}
