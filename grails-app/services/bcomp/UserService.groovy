package bcomp

import bcomp.aaa.Role
import bcomp.aaa.User
import bcomp.aaa.UserRole
import bcomp.gym.Grade

class UserService {

    public final static String BOULDERER_AUTHORITY = "ROLE_BOULDERER"

    /**
     * Registers a new user with the boulderer role.
     */
    public void registerBoulderer(User user) {
        resetCurrentBouldererGrades(user);

        user.save()

        Role bouldererRole = Role.findByAuthority(BOULDERER_AUTHORITY);
        UserRole.create(user, bouldererRole)
    }

    def resetCurrentBouldererGrades(User user) {
        // probability should be 90% that personal level is at most two font grades away from personal level
        // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
        double variance = (2 * Grade.oneFontGradeDifference() / 1.645)**2

        user.currentGrade = user.initialGrade
        user.currentGradeVariance = variance
    }

}
