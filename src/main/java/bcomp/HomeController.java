package bcomp;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String home(HttpServletRequest req, final ModelMap m) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			m.addAttribute("user", user.getNickname());
			return "home";
		} else {
			return "redirect:http://localhost:8080" + userService.createLoginURL(req.getRequestURI());
		}
	}

}