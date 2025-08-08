package com.hana7.hanaro.repository;

import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByMember(Member member);
}
