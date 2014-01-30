package bcomp

import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.rest.RestfulController


class GymController extends RestfulController {

    static responseFormats = ['json', 'html']

    GymController() {
        super(Gym)
    }

    def boulders() {
        def gymId = params.gymId
        def boulders = Boulder.where {
            gym.id == gymId
        }.findAll()
        respond boulders, [formats: ['json']]
    }

}
