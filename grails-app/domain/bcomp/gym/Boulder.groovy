package bcomp.gym

class Boulder {

    static constraints = {
        grade nullable: false
        locationDescription nullable: true
    }

    static belongsTo = [gym: Gym]

    static hasOne = [location: Location]

    String locationDescription

    String grade

    public void onFloorPlan(FloorPlan floorPlan, double x, double y) {
        location = new OnFloorPlan(floorPlan: floorPlan, x: x, y: y)
        location.boulder = this
    }

}
