import bcomp.BoulderingSessionController

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.${format})?" {
            constraints {
                // apply constraints here
            }
        }

        "/leaderboard"(controller: 'leaderboard', action: 'view')


        "/gyms/$gymId/floorPlans/$floorPlanId"(controller: 'floorPlan', action: 'image')

        "/boulderer/$username/ascents"(controller: 'boulderer', action: 'listAscents')
        "/boulderer/$username/statistics"(controller: 'boulderer', action: 'statistics')


        "/"(controller: 'index', action: 'index')

        "/status"(view: "/index")
        "500"(view: '/error')



        "/grades"(resource: "grade")

        "/gyms"(resources: "gym") {
            "/boulders"(controller: 'gym', action: 'boulders')
        }
        "/boulders"(resources: "boulder")

        "/users"(resources: "user") {
            "/statistics"(controller: 'user', action: 'statistics')
        }
        "/sessions"(resources: 'boulderingSession')
    }
}
