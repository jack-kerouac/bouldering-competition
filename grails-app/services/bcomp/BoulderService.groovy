package bcomp

import bcomp.gym.Boulder
import bcomp.gym.Grade
import bcomp.gym.Gym

/**
 * User: florian
 */
class BoulderService {

    /**
     * Creates a new boulder (to set == "schrauben").
     *
     * @param gym an existing gym
     * @param boulder a new boulder
     */
    public void setBoulder(Gym gym, Boulder boulder) {
        gym.attach()
        resetCurrentBoulderGrade(boulder);

        gym.addToBoulders(boulder);
    }


    public void resetCurrentBoulderGrade(Boulder boulder) {
        if (boulder.hasAssignedGrade()) {
            boulder.currentGrade = boulder.getAssignedGrade();
            // probability should 90% that real grade is at most one font grade away from initial grade
            // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
            boulder.currentGradeVariance = (1 * Grade.oneFontGradeDifference() / 1.645)**2;
        } else if (boulder.hasGradeRange()) {
            boulder.currentGrade = Grade.between(boulder.getGradeRangeLow(), boulder.getGradeRangeHigh());
            // probability should be 90% that real grade is at most one font grade outside of initial range
            // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
            double diff = (boulder.getGradeRangeHigh() - boulder.getGradeRangeLow()) / 2 + Grade
                    .oneFontGradeDifference()
            boulder.currentGradeVariance = (diff / 1.645)**2;
        } else {
            boulder.currentGrade = Grade.between(Grade.lowest(), Grade.highest());
            // TODO: is this a good initial sigma?
            // almost all values should be within the range of grades
            // => sigma = 3 (see http://en.wikipedia.org/wiki/68%E2%80%9395%E2%80%9399.7_rule)
            double diff = (Grade.highest() - Grade.lowest()) / 2
            boulder.currentGradeVariance = (diff / 3)**2;
        }
    }

}
