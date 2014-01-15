import bcomp.HomeController

class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

        "/leaderboard"(controller: 'leaderboard', action: 'view')

        "/gyms/$gymId/floorPlans/$floorPlanId"(controller: 'floorPlan', action: 'image')

        "/boulderer/$username/ascents"(controller: 'boulderer', action: 'listAscents')

        "/"(controller: 'home', action: 'home')

        "/foundation"(view:'/foundation')


        "/status"(view:"/index")
        "500"(view:'/error')
	}
}
