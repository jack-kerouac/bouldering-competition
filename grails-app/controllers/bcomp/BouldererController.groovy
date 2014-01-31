package bcomp

import bcomp.aaa.User
import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Secured(['ROLE_BOULDERER'])
class BouldererController {

    def listAscents(String username) {
        def _gym = Gym.findByName('Boulderwelt')
        def boulderer_ = User.findByUsername(username)
        if (boulderer_ == null)
            throw new UsernameNotFoundException("boulderer ${username} not found")
        def sessions = BoulderingSession.where {
            boulderer == boulderer_
            gym == _gym
        }.order('date', 'desc')

        def ascents = [:]

        sessions.each { session ->
            session.ascents.each { ascent ->
                Boulder boulder = ascent.boulder
                if (!ascents.containsKey(boulder))
                    ascents[boulder] = []
                def a = [:]
                a['date'] = session.date
                a['style'] = ascent.style
                ascents[boulder] << a
            }
        }

        render view: 'listAscents', model: [gym: _gym, boulderer: boulderer_, ascents: ascents]
    }

}
