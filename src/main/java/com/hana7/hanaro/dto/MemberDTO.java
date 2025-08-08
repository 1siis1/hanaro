package com.hana7.hanaro.dto;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.hana7.hanaro.entity.MemberRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class MemberDTO extends User {
	private Long id;
	private String email;
	private String nickname;
	private MemberRole role; // Member 엔티티와 일치하도록 수정

	public MemberDTO(Long id, String email, String password, String nickname, MemberRole role) {
		super(email, password, List.of(new SimpleGrantedAuthority(role.name())));
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.role = role;
	}

	public List<String> getRoleNames() {
		return List.of(this.role.name());
	}
}
