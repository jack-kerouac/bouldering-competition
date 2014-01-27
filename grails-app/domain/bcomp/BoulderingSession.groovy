package bcomp

import bcomp.aaa.User
import bcomp.gym.Gym

class BoulderingSession {

    static belongsTo = [gym: Gym, boulderer: User]

    static hasMany = [ascents: Ascent]

    Date date

    static constraints = {
        ascents validator: { ascents, session ->
            return ascents.every { it.boulder.gym == session.gym }
        }
    }

}
