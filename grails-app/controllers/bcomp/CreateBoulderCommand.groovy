package bcomp

import grails.validation.Validateable

@Validateable
class CreateBoulderCommand {

    String grade
    Section section

    static constraints = {
        importFrom Boulder
    }

}
