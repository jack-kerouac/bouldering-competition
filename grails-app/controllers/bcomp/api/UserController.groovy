package bcomp.api

import bcomp.aaa.User
import bcomp.gym.Grade
import grails.rest.RestfulController
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserController extends RestfulController {

    static responseFormats = ['json']

    def bouldererService

    def authenticationManager

    UserController() {
        super(User)
    }

    def index(String email, Integer max) {
        if (email != null) {
            respond User.findByUsername(email)
        } else {
            super.index(max)
        }
    }

    def statistics(Long userId) {
        def boulderer = User.findById(userId)
        if (boulderer == null)
            throw new UsernameNotFoundException("boulderer ${userId} not found")

        def stats = bouldererService.getSessionStatistics(boulderer)

        respond stats
    }


    def loginOrRegister(Credentials c) {
        User user = User.findByUsername(c.email);
        if(user != null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(c.email, c.password);
            token.setDetails(user);

            try {
                Authentication auth = authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                respond new LoginStatus(loggedIn: auth.isAuthenticated(), email: auth.getName(), userId: user.id);
            } catch (BadCredentialsException e) {
                respond new LoginStatus(loggedIn: false);
            }
        }
        else {
            user = new User(username: c.email, password: c.password, initialGrade: Grade.fromFontScale('5a'))
            bouldererService.registerBoulderer(user)
            loginOrRegister(c)
        }
    }

}

class Credentials {
    String email
    String password
}

class LoginStatus {
    boolean loggedIn
    String email
    Long userId
}