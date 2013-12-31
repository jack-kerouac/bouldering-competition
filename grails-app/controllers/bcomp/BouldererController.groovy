package bcomp

import bcomp.aaa.User
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Secured(['ROLE_BOULDERER'])
class BouldererController {

    def listAscents(String username) {
        def gym = Gym.findByName('Heavens Gate')
        def boulderer_ = User.findByUsername(username)
        if(boulderer_ == null)
            throw new UsernameNotFoundException("boulderer ${username} not found")
        def ascents = Ascent.where {
            boulderer == boulderer_
            boulder.section.gym == gym
        }.order('date', 'desc')
        render view: 'listAscents', model: [gym: gym, boulderer: boulderer_, ascents: ascents]
    }
}
