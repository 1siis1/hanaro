package com.hana7.hanaro.member;

import com.hana7.hanaro.RepositoryTest;
import com.hana7.hanaro.member.entity.Role;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Rollback(false)
class MemberRepositoryTest extends RepositoryTest {
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	private MemberRepository memberRepository;

	private Member testMember;
	//
	// @BeforeEach
	// void clear() {
	// 	memberRepository.deleteAll();
	// }

	@BeforeEach
	void setUp() {
		Member member = Member.builder()
			.email("testuser@gmail.com")
			.password(encoder.encode("test12345"))
			.nickname("testuser")
			.role(Role.USER)
			.build();
		testMember = memberRepository.save(member);
	}



	@Test
	@Order(1)
	void createMemberTest() {
		Member member = Member.builder()
			.email("tester@gmail.com")
			.password(encoder.encode("test12345"))
			.nickname("tester")
			.role(Role.USER)
			.build();

		Member savedMember = memberRepository.save(member);

		assertThat(savedMember.getId()).isNotNull();
		assertThat(savedMember.getEmail()).isEqualTo("tester@gmail.com");
		assertThat(savedMember.getNickname()).isEqualTo("tester");
		assertThat(savedMember.getRole()).isEqualTo(Role.USER);
	}

	@Test
	@Order(2)
	void findByEmailTest() {
		Member foundMember = memberRepository.findByEmail("testuser@gmail.com").orElse(null);

		assertThat(foundMember).isNotNull();
		assertThat(foundMember.getEmail()).isEqualTo(testMember.getEmail());
	}

	@Test
	@Order(3)
	void updateMemberTest() {
		Member memberToUpdate = memberRepository.findById(testMember.getId()).orElseThrow();
		Member updatedMember = Member.builder()
			.id(memberToUpdate.getId())
			.email(memberToUpdate.getEmail())
			.password(memberToUpdate.getPassword())
			.nickname("updatedUser")
			.role(memberToUpdate.getRole())
			.build();
		memberRepository.save(updatedMember);

		Member foundMember = memberRepository.findById(testMember.getId()).orElseThrow();
		assertThat(foundMember.getNickname()).isEqualTo("updatedUser");
	}

	@Test
	@Order(4)
	void deleteMemberTest() {
		memberRepository.deleteById(testMember.getId());

		assertThat(memberRepository.findById(testMember.getId())).isEmpty();
	}
}
