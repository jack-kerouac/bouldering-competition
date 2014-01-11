package bcomp

import bcomp.aaa.User
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured

@Secured()
class GradeLearningController {

    def gradeLearningService

    def showMeta() {
        def gym = Gym.findByName('Boulderwelt')
        def users = [:]
        for(User user : User.findAll()){
            def ascents = Ascent.findByBoulderer(user) ?: [] as Set
            users.put(user, ascents)
        }

        [users: users, boulders: gym.boulders]
    }

    def resetCurrentUserGrades() {
        gradeLearningService.resetCurrentUserGrades()
        redirect action: 'showMeta'
    }

    def resetCurrentBoulderGrades() {
        gradeLearningService.resetCurrentBoulderGrades()
        redirect action: 'showMeta'
    }

}
