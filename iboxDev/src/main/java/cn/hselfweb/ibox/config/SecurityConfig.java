package cn.hselfweb.ibox.config;

/*import cn.hselfweb.ibox.db.User;
import cn.hselfweb.ibox.service.SpringDataJpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpringDataJpaUserDetailsService userDetailsService;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(this.userDetailsService)
//                .passwordEncoder(User.PASSWORD_ENCODER);
//    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic()
                .and().authorizeRequests()
                .antMatchers("/index.html", "/","/api").permitAll().anyRequest().authenticated()
                .antMatchers("/test").access("hasRole('ROLE_ADMIN')")
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
       ;
//                .and().formLogin().loginPage("/login")
//                .defaultSuccessUrl("/welcome").failureUrl("/login?error")
//                .usernameParameter("user-name").passwordParameter("pwd")
//                .and()
//                .logout().logoutSuccessUrl("/login?logout");
//    }
    }
}*/
