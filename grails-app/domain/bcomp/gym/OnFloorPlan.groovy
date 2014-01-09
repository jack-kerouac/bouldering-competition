package bcomp.gym

class OnFloorPlan extends Location {

    static constraints = {
        floorPlan nullable: false, validator: { floorPlan, location ->
            return location.boulder.gym.floorPlans.contains(floorPlan)
        }
        x min: 0.0d, max: 1.0d
        y min: 0.0d, max: 1.0d
    }

    FloorPlan floorPlan

    double x, y

}
