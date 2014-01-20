package bcomp

import bcomp.gym.*

import javax.imageio.ImageIO

class SampleData {

    public static Gym createGym(String name, def grailsApplication) {
        Gym gym = new Gym(name)

        def filePath = 'resources/halle_big.jpg'
        def imageFile = grailsApplication.getParentContext().getResource("classpath:$filePath").getFile()
        FloorPlan fp = new FloorPlan(ImageIO.read(imageFile))
        gym.addToFloorPlans(fp)
    }

    public static Boulder createBoulder1(FloorPlan fp) {
        Boulder b = new Boulder(color: BoulderColor.RED)
        b.onFloorPlan(fp, 534 / 2000, 298 / 1393)
        b.gradeRange(Grade.fromFontScale('1A'), Grade.fromFontScale('8A'))
        return b
    }

    public static Boulder createBoulder2(FloorPlan fp) {
        Boulder b = new Boulder(color: BoulderColor.RED)
        b.onFloorPlan(fp, 743 / 2000, 343 / 1393)
        b.gradeRange(Grade.fromFontScale('1A'), Grade.fromFontScale('8A'))
        return b
    }

    public static Boulder createBoulder3(FloorPlan fp) {
        Boulder b = new Boulder(color: BoulderColor.WHITE)
        b.onFloorPlan(fp, 566 / 2000, 292 / 1393)
        b.unknownGrade()
        return b
    }

    public static Boulder createBoulder4(FloorPlan fp) {
        Boulder b = new Boulder(color: BoulderColor.WHITE)
        b.onFloorPlan(fp, 612 / 2000, 481 / 1393)
        b.gradeRange(Grade.fromFontScale('5C'), Grade.fromFontScale('6a+'))
        return b
    }

    public static Boulder createBoulder5(FloorPlan fp) {
        Boulder b = new Boulder(color: BoulderColor.BLACK)
        b.onFloorPlan(fp, 751 / 2000, 659 / 1393)
        b.gradeRange(Grade.fromFontScale('6b'), Grade.fromFontScale('7a'))
        return b
    }

    public static Boulder createBoulder6(FloorPlan fp) {
        Boulder b = new Boulder(color: BoulderColor.YELLOW_BLACK)
        b.onFloorPlan(fp, 783 / 2000, 366 / 1393)
        b.gradeRange(Grade.fromFontScale('3B'), Grade.fromFontScale('4A'))
        return b
    }

    public static Boulder createBoulder7(FloorPlan fp) {
        Boulder b = new Boulder(color: BoulderColor.RED)
        b.onFloorPlan(fp, 0.4056701030927835, 0.8858173076923077)
        b.assignedGrade(Grade.fromFontScale('6c'))
        return b
    }

    public static Boulder createBoulder8(FloorPlan fp) {
        Boulder b = new Boulder(color: BoulderColor.BROWN)
        b.onFloorPlan(fp, 0.5128865979381443, 0.8636279585798816)
        b.assignedGrade(Grade.fromFontScale('7a+'))
        return b
    }

    public static Boulder createBoulder9(FloorPlan fp) {
        Boulder b = new Boulder(color: BoulderColor.YELLOW)
        b.onFloorPlan(fp, 0.5304123711340206, 0.8680658284023669)
        b.assignedGrade(Grade.fromFontScale('7a'))
        return b
    }

}