/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.repos;


import io.ismaiel.hasebona.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ismaiel
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByGoogleId(String googleId);

    public User findByEmail(String userEmail);
}
