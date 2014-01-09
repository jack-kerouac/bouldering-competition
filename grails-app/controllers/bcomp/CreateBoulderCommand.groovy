package bcomp

import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.validation.Validateable

@Validateable
class CreateBoulderCommand {

    Gym gym

    static constraints = {
        importFrom Boulder
    }

}
