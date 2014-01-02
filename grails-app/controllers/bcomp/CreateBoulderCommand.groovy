package bcomp

import bcomp.gym.Boulder
import bcomp.gym.Section
import grails.validation.Validateable

@Validateable
class CreateBoulderCommand {

    String grade
    Section section

    String locationDescription

    static constraints = {
        importFrom Boulder
    }

}
