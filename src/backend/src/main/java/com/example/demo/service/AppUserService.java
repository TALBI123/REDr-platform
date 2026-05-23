import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;
    
}
