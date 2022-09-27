package dev.arthurcech.supportportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.arthurcech.supportportal.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findUserByUsername(String username);

	User findUserByEmail(String email);

}
