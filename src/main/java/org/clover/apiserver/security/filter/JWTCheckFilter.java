package org.clover.apiserver.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.clover.apiserver.dto.MemberDTO;
import org.clover.apiserver.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    //로그인 할 때는 당연히 토큰이 없으니까 filtering하지 않도록 해주어야함

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        //true == not checking
        String path = request.getRequestURI();
        log.info("check uri------------" + path);

        if(path.startsWith("/api/member/")){
            return true;
        }


        //false == check
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("--------------");
        log.info("--------------");
        log.info("--------------");

        String authHeaderStr = request.getHeader("Authorization");

        //Bearer //7 JWT문자열
        try {
            String accessToken = authHeaderStr.substring(7);
            Map<String,Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);

            //destination -> 목적지를 알려줌(성공하면 다음꺼 타!)
            //filterChain.doFilter(request, response);

            String email = (String) claims.get("email");
            String pw = (String) claims.get("pw");
            String nickname = (String) claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(), roleNames);

            log.info("------------------------");
            log.info(memberDTO);
            log.info(memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request,response);


        } catch (Exception e) {

            log.error("JWT Check Error.................");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();

        }


    }
}
