package bcomp

import bcomp.gym.Boulder
import bcomp.gym.Gym
import bcomp.gym.TentativeGrade

class BoulderService {

    def gradeLearningService

    /**
     * Creates a new boulder (to set == "schrauben").
     *
     * @param gym an existing gym
     * @param boulder a new boulder
     */
    public void setBoulder(Gym gym, Boulder boulder) {
        gym.attach()

        TentativeGrade initial = gradeLearningService.calculateInitialGrade(boulder)
        boulder.setGrade(initial)

        gym.addToRoutes(boulder);
    }

}
