package bcomp.api

import bcomp.BoulderingSession
import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

class GymController extends RestfulController {

    static responseFormats = ['json']

    GymController() {
        super(Gym)
    }

    def boulders() {
        def gymId = params.gymId
        def boulders = Boulder.where {
            gym.id == gymId
        }.findAll()
        respond boulders
    }

    def sessions() {
        def gymId = params.gymId
        def sessions = BoulderingSession.where {
            gym.id == gymId
        }.findAll()
        respond sessions
    }

    @Override
    @Secured(['ROLE_BOULDERER'])
    def delete() {
        return super.delete()
    }

    @Secured(['ROLE_BOULDERER'])
    @Override
    def update() {
        return super.update()
    }

    @Secured(['ROLE_BOULDERER'])
    @Override
    def save() {
        return super.save()
    }
}
