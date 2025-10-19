package com.imooc.mumushengxian.config;

import com.imooc.mumushengxian.filter.AdminInterceptor;
import com.imooc.mumushengxian.filter.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决拦截器中依赖注入失败的关键是：
     * 将拦截器声明为Spring Bean（使用@Component）
     * 在配置类中注入拦截器实例（使用@Autowired）
     * 使用构造器注入依赖（最佳实践）
     * 确保正确的拦截器执行顺序
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final UserInterceptor userInterceptor;    // 使用Spring管理的Bean
    private final AdminInterceptor adminInterceptor;

    @Autowired
    public InterceptorConfig(UserInterceptor userInterceptor, AdminInterceptor adminInterceptor) {
        this.userInterceptor = userInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 普通用户拦截器
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/user/**") // 拦截用户相关路径
                .excludePathPatterns("/user/login", "/user/register", "/user/logout", "/user/product/*")  // 排除不需要拦截的路径
                .order(1);  //当有多个拦截器时，明确执行顺序
        // 管理员拦截器
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**") // 拦截管理员相关路径
                .excludePathPatterns("/admin/login", "/admin/logout")  // 排除不需要拦截的路径
                .order(2);  //当有多个拦截器时，明确执行顺序
    }
}
