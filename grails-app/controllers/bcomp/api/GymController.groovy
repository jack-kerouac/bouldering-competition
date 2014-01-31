package bcomp.api

import bcomp.gym.Boulder
import bcomp.gym.FloorPlan
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

    def floorPlanImage(Long gymId, Long floorPlanId) {
        cache shared: true, validFor: 3600  // 1hr on content

        Gym gym = Gym.findById(gymId)
        assert gym.floorPlans.any { it.id == floorPlanId}
        FloorPlan fp = FloorPlan.findById(floorPlanId)

        response.setContentType("image/png")
        response.outputStream << fp.getImageAsInputStream()
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
