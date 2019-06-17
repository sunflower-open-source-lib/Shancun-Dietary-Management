package cn.hselfweb.ibox.service;

//import cn.hselfweb.ibox.db.User;
//import cn.hselfweb.ibox.db.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
////@Component
//public class DatabaseLoader implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    @Autowired
//    public DatabaseLoader(UserRepository userRepository) {
//
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public void run(String... strings) throws Exception {
//
//        User greg = this.userRepository.save(new User((long)6, "turnquist",
//                "111","111",new String[]{"ROLE_ADMIN"}));
//        User oliver = this.userRepository.save(new User((long)7, "gierke",
//                "222","111",new String[]{"ROLE_ADMIN"}));
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken("greg", "doesn't matter",
//                        AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
//        SecurityContextHolder.clearContext();
//    }
//}