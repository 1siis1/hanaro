package com.hana7.hanaro.repository;

import com.hana7.hanaro.entity.Member;
import com.hana7.hanaro.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByMember(Member member);
}
