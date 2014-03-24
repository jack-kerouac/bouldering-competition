package bcomp.gym

abstract class Route {

    enum GradeCertainty {
        UNKNOWN, RANGE, ASSIGNED;
    }

    static mapping = {
        end column: '"end"'
        discriminator column: "route_type"
    }

    static constraints = {
        foreignId nullable: true, unique: 'gym'

        name nullable: true
        description nullable: true
        end nullable: true
    }

    static belongsTo = [gym: Gym]

    static hasOne = [location: Location]

    String name

    Long foreignId

    /**
     * Either the color of the holds and feet or any other marks to distinguish this route from others.
     */
    RouteColor color

    Date dateCreated
    Date lastUpdated


    Date end

    String description

    public final void onFloorPlan(FloorPlan floorPlan, double x, double y) {
        location = new OnFloorPlan(floorPlan: floorPlan, x: x, y: y)
        location.route = this
    }

}
