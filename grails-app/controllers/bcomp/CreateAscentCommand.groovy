package bcomp

import grails.validation.Validateable

@Validateable
class CreateAscentCommand {

    Date date = new Date()
    Ascent.Style style = Ascent.Style.flash
    int tries = 2

    Boulder boulder
    Boulderer boulderer

    static constraints = {
        importFrom Ascent
    }

}
