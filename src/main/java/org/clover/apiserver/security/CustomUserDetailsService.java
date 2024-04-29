package org.clover.apiserver.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.clover.apiserver.domain.Member;
import org.clover.apiserver.dto.MemberDTO;
import org.clover.apiserver.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override          //UserDetails : MemberDTO, username: id(email)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("------------loadUserByUsername-----------" + username);

        Member member = memberRepository.getWithRoles(username);

        if(member == null) {
            throw new UsernameNotFoundException("Not Found");
        }

        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList() //List<MemberRole> -> List<String>으로 바꿔주기 위함
                        .stream()
                        .map(memberRole -> memberRole.name()).collect(Collectors.toList()));

        return memberDTO;
    }
}
