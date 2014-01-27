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

        "/"(controller: 'home', action: 'home')

        "/status"(view: "/index")
        "500"(view: '/error')



        "/gyms"(resources: "gym") {
            "/boulders"(controller: 'gym', action: 'boulders')
        }
        "/boulders"(resources: "boulder")

        "/users"(resources: "user")
        "/sessions"(resources: 'boulderingSession')
    }
}