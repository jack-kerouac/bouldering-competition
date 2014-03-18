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
            // TODO: reenable this
            //super.index(max)
            render status: 404
        }
    }

    @Override
    def delete() {
        // TODO: reenable this
        render status: 404
    }

    @Override
    def update() {
        // TODO: reenable this
        render status: 404
    }

    def statistics(Long userId) {
        def boulderer = User.findById(userId)
        if (boulderer == null)
            throw new UsernameNotFoundException("boulderer ${userId} not found")

        def stats = bouldererService.getSessionStatistics(boulderer)

        respond stats
    }


    // based on http://raibledesigns.com/rd/entry/implementing_ajax_authentication_using_jquery
    def loginOrRegister(Credentials c) {
        c.email = c.email.toLowerCase()
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

    // based on http://raibledesigns.com/rd/entry/implementing_ajax_authentication_using_jquery
    def loginStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getName().equals("__grails.anonymous.user__") && auth.isAuthenticated()) {
            respond new LoginStatus(loggedIn: true, email: auth.getName(), userId: auth.details.id);
        } else {
            respond new LoginStatus(loggedIn: false);
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