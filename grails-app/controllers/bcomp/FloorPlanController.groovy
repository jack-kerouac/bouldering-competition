package bcomp

import bcomp.gym.FloorPlan
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_BOULDERER'])
class FloorPlanController {

    def image(Long gymId, Long floorPlanId) {
        Gym gym = Gym.findById(gymId)
        assert gym.floorPlans.any { it.id == floorPlanId}
        FloorPlan fp = FloorPlan.findById(floorPlanId)

        response.setContentType("image/png")
        response.outputStream << fp.getImageAsInputStream()
    }

}
