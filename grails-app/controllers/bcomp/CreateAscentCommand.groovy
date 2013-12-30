package bcomp

import bcomp.aaa.User
import grails.validation.Validateable

@Validateable
class CreateAscentCommand {

    Date date = new Date()
    Ascent.Style style = Ascent.Style.flash
    int tries = 2

    Boulder boulder
    User boulderer

    static constraints = {
        importFrom Ascent
    }

}
