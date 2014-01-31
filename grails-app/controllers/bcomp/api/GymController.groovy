package bcomp.api

import bcomp.gym.Boulder
import bcomp.gym.FloorPlan
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

@Secured(['ROLE_BOULDERER'])
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

}
