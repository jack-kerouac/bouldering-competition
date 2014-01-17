package bcomp

import bcomp.aaa.User
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Secured(['ROLE_BOULDERER'])
class BouldererController {

    def listSessions(String username) {
        def _gym = Gym.findByName('Boulderwelt')
        def boulderer_ = User.findByUsername(username)
        if(boulderer_ == null)
            throw new UsernameNotFoundException("boulderer ${username} not found")
        def sessions = BoulderingSession.where {
            boulderer == boulderer_
            gym == _gym
        }.order('date', 'desc')
        render view: 'listSessions', model: [gym: _gym, boulderer: boulderer_, sessions: sessions]
    }
}
