package bcomp

import bcomp.aaa.User
import bcomp.gym.Boulder

class Ascent {

    enum Style {
        flash, top
    }

    Date date
    Style style

    static belongsTo = [boulder: Boulder, boulderer: User]

    static constraints = {
        boulder nullable: false
    }
}

