package com.hana7.hanaro.config;

import com.hana7.hanaro.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;

@Getter
public class UserPrincipal extends User {
	private final Member member;

	public UserPrincipal(Member member) {
		super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().name())));
		this.member = member;
	}
}
