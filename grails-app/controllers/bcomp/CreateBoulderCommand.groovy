package bcomp

import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.validation.Validateable

@Validateable
class CreateBoulderCommand {

    String grade
    Gym gym

    String locationDescription

    static constraints = {
        importFrom Boulder
    }

}
