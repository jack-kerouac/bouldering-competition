package bcomp

import bcomp.gym.*

class SampleData {

    public static Gym createBoulderwelt(def grailsApplication) {
        Gym gym = new Gym("Boulderwelt München")

        def linkGenerator = grailsApplication.mainContext.getBean("grailsLinkGenerator")
        def imageUrl = linkGenerator.resource(dir: 'images', file: 'floorPlans/boulderwelt-muenchen.jpg',
                absolute: false)

        FloorPlan fp = new FloorPlan(widthInPx: 2000, heightInPx: 1393, imageUrl: imageUrl)
        gym.addToFloorPlans(fp)
    }

    public static Gym createHeavensGate(def grailsApplication) {
        Gym gym = new Gym("Heavens Gate")

        def linkGenerator = grailsApplication.mainContext.getBean("grailsLinkGenerator")
        def imageUrl = linkGenerator.resource(dir: 'images', file: 'floorPlans/heavens-gate.png',
                absolute: false)

        FloorPlan fp = new FloorPlan(widthInPx: 1304, heightInPx: 1393, imageUrl: imageUrl)
        gym.addToFloorPlans(fp)
    }

    public static Boulder createBoulder1(FloorPlan fp) {
        Boulder b = new Boulder(color: RouteColor.RED)
        b.onFloorPlan(fp, 534 / 2000, 298 / 1393)
        b.gradeRange(BoulderGrade.fromFontScale('1A'), BoulderGrade.fromFontScale('8A'))
        b.end = new Date(114, 1, 1)
        return b
    }

    public static Boulder createBoulder2(FloorPlan fp) {
        Boulder b = new Boulder(color: RouteColor.RED)
        b.onFloorPlan(fp, 743 / 2000, 343 / 1393)
        b.gradeRange(BoulderGrade.fromFontScale('1A'), BoulderGrade.fromFontScale('8A'))
        return b
    }

    public static Boulder createBoulder3(FloorPlan fp) {
        Boulder b = new Boulder(color: RouteColor.WHITE)
        b.onFloorPlan(fp, 566 / 2000, 292 / 1393)
        b.unknownGrade()
        return b
    }

    public static Boulder createBoulder4(FloorPlan fp) {
        Boulder b = new Boulder(color: RouteColor.WHITE)
        b.onFloorPlan(fp, 612 / 2000, 481 / 1393)
        b.gradeRange(BoulderGrade.fromFontScale('5C'), BoulderGrade.fromFontScale('6a+'))
        return b
    }

    public static Boulder createBoulder5(FloorPlan fp) {
        Boulder b = new Boulder(color: RouteColor.BLACK)
        b.onFloorPlan(fp, 751 / 2000, 659 / 1393)
        b.gradeRange(BoulderGrade.fromFontScale('6b'), BoulderGrade.fromFontScale('7a'))
        return b
    }

    public static Boulder createBoulder6(FloorPlan fp) {
        Boulder b = new Boulder(color: RouteColor.YELLOW_BLACK)
        b.onFloorPlan(fp, 783 / 2000, 366 / 1393)
        b.gradeRange(BoulderGrade.fromFontScale('3B'), BoulderGrade.fromFontScale('4A'))
        return b
    }

    public static Boulder createBoulder7(FloorPlan fp) {
        Boulder b = new Boulder(color: RouteColor.RED)
        b.onFloorPlan(fp, 0.4056701030927835, 0.8858173076923077)
        b.assignedGrade(BoulderGrade.fromFontScale('6c'))
        return b
    }

    public static Boulder createBoulder8(FloorPlan fp) {
        Boulder b = new Boulder(color: RouteColor.BROWN)
        b.onFloorPlan(fp, 0.5128865979381443, 0.8636279585798816)
        b.assignedGrade(BoulderGrade.fromFontScale('7a+'))
        return b
    }

    public static Boulder createBoulder9(FloorPlan fp) {
        Boulder b = new Boulder(color: RouteColor.YELLOW)
        b.onFloorPlan(fp, 0.5304123711340206, 0.8680658284023669)
        b.assignedGrade(BoulderGrade.fromFontScale('7a'))
        return b
    }

}