package com.temp.upload.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

@Service
public class UploadUtil {

    private static final Logger logger = LoggerFactory.getLogger(UploadUtil.class);

    UploadUtil()
    {
        FirebaseApp firebaseApp = null;
        try {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/firebase-config.json")))
                    .setStorageBucket("upload-project-firebase.appspot.com")
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options);
        } catch (Exception exception) {
            logger.error("Exception: " + exception.getMessage());
        }
    }


    public Firestore getFireStore() {
        return FirestoreClient.getFirestore();
    }

    public Bucket getBucket()
    {
        return StorageClient.getInstance().bucket();
    }

    public long timeDiffNow(String docDate)
    {
            Long currentTimeStamp = System.currentTimeMillis();
            logger.debug("Current time stamp: "+currentTimeStamp);

            long difference_In_Time = currentTimeStamp - Long.parseLong(docDate);

            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24));

            long difference_In_Minutes
                = (difference_In_Time
                / (1000 * 60));

//            logger.debug("Difference in minutes: "+ difference_In_Minutes);
            logger.debug("Difference in Days: "+difference_In_Days);
            return difference_In_Days;
            }
}
