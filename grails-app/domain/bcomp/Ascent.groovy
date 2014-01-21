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

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Ascent)) return false

        Ascent ascent = (Ascent) o

        if (id != ascent.id) return false

        return true
    }

    int hashCode() {
        return (id != null ? id.hashCode() : 0)
    }
}

