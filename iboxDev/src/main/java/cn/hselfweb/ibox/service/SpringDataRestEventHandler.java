package cn.hselfweb.ibox.service;
//
//import cn.hselfweb.ibox.db.User;
//import cn.hselfweb.ibox.db.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
//import org.springframework.data.rest.core.annotation.HandleBeforeSave;
//import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;

//@Component
//@RepositoryEventHandler(User.class)
//public class SpringDataRestEventHandler {
//    private final UserRepository userRepository;
//
//    @Autowired
//    public SpringDataRestEventHandler(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

//    @HandleBeforeCreate
//    @HandleBeforeSave
//    public void applyUserInformationUsingSecurityContext(User user) {
//
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        User manager = this.userRepository.findByTel(name);
//        if (manager == null) {
//            Manager newManager = new Manager();
//            newManager.setName(name);
//            newManager.setRoles(new String[]{"ROLE_MANAGER"});
//            manager = this.managerRepository.save(newManager);
//        }
//        employee.setManager(manager);
//    }
//}

