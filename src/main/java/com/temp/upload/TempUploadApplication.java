package com.temp.upload;

import com.temp.upload.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class TempUploadApplication {

    @Autowired
    private UploadService uploadService;

    private static final Logger logger = LoggerFactory.getLogger(TempUploadApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TempUploadApplication.class, args);
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void perform()
    {
        logger.info("Scheduled method started");
        uploadService.deleteFiles();
        logger.info("Scheduled method ended");
    }
}
