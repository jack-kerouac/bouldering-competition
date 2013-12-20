package bcomp

import grails.validation.Validateable

@Validateable
class CreateAscentCommand {

    Date date = new Date()
    Ascent.Style style = Ascent.Style.flash
    int tries = 1

    Boulder boulder

    static constraints = {
        importFrom Ascent
    }

}
