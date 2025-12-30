package hu.project.smartmealfinderb.Config.Jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareJpaImpl")
public class JpaConfig {
}
