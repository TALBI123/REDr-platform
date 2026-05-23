import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller
public class AppUserController {
    @Autowired
    private AppUserService appUserService;
}
