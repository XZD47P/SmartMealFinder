package hu.project.smartmealfinderb.Config;

import com.recombee.api_client.RecombeeClient;
import com.recombee.api_client.util.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecombeeConfig {

    @Value("${recombee.database_id}")
    private String databaseId;
    @Value("${recombee.private_token}")
    private String privateToken;

    @Bean
    public RecombeeClient recombeeClient() {
        return new RecombeeClient(this.databaseId, this.privateToken)
                .setRegion(Region.EU_WEST);
    }
}
