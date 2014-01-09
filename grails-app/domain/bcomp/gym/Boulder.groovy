package bcomp.gym

class Boulder {

    static constraints = {
        color nullable: false
        locationDescription nullable: true
    }

    static belongsTo = [gym: Gym]

    static hasOne = [location: Location]

    String locationDescription

    /**
     * Either the color of the holds and feet of the boulder or any other marks to distinguish this boulder from
     * others.
     */
    BoulderColor color

    public void onFloorPlan(FloorPlan floorPlan, double x, double y) {
        location = new OnFloorPlan(floorPlan: floorPlan, x: x, y: y)
        location.boulder = this
    }

}
