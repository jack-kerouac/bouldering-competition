package bcomp

import bcomp.aaa.User
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured

@Secured()
class LearningAlgoController {

    def showMeta() {
        def gym = Gym.findByName('Boulderwelt')
        def users = [:]
        for(User user : User.findAll()){
            def ascents = Ascent.findByBoulderer(user) ?: [] as Set
            users.put(user, ascents)
        }

        [users: users, boulders: gym.boulders]
    }

}
