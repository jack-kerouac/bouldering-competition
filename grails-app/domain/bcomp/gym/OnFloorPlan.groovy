package bcomp.gym

class OnFloorPlan extends Location {

    static constraints = {
        floorPlan nullable: false, validator: { floorPlan, location ->
            return location.boulder.gym.floorPlans.contains(floorPlan)
        }
        x validator: { x, location ->
            return x in (0..<location.floorPlan.widthInPx)
        }
        y validator: { y, location ->
            return y in (0..<location.floorPlan.heightInPx)
        }
    }

    FloorPlan floorPlan

    int x, y

}
