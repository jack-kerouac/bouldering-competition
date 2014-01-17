package bcomp

import bcomp.gym.Boulder

class Ascent {

    enum Style {
        flash, top
    }

    Style style

    static belongsTo = [boulder: Boulder, session: BoulderingSession]

    static constraints = {
        boulder nullable: false
        session nullable: false
    }

    // useful since we often handle ascents themselves
    def getBoulderer() {
        return session.boulderer
    }

}

