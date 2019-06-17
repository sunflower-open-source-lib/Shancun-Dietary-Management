package cn.hselfweb.ibox.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@Setter
//@ToString(exclude = "password")
@Table(name = "user")
public class User {
    /**
     * PASSWORD_ENCODER 是在比较之前加密新密码或接受密码输入并加密它们的方法。
     * <p>
     * id，name，password，和roles定义来限制访问所需的参数。
     * <p>
     * 自定义setPassword()确保密码永远不会以明文形式存储。
     */

    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    /**
     * @ToString(exclude = "password") 确保Lombok生成的toString（）方法不会打印出密码。
     * @JsonIgnore 应用于密码字段保护json序列化该字段。
     */
    @Column(name = "password")
    private /*@JsonIgnore*/ String password;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "tel")
    private String tel;

    @Column(name = "info")
    private String info;

    @Column(name = "head_url")
    private String headUrl;


    //private String[] roles;

    /*public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }*/
}
