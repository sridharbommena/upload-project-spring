package com.temp.upload.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.temp.upload.service.UploadService;
import com.temp.upload.util.UploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UploadUtil uploadUtil;

    private final String GSURL_PREFIX="gs://upload-project-firebase.appspot.com/";
    private final String COLLECTION_NAME = "upload-project";

    private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Override
    public void deleteFiles() {
        logger.debug("Delete Task started");
        Firestore fireStoreDb = uploadUtil.getFireStore();
        ApiFuture<QuerySnapshot> query = fireStoreDb.collection(COLLECTION_NAME).get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException e) {
            logger.error("Error: "+e.getMessage());
        } catch (ExecutionException e) {
            logger.error("Error: "+e.getMessage());
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        for (QueryDocumentSnapshot queryFile:documents) {
            DocumentReference reference = queryFile.getReference();
            String timestamp = (String) queryFile.get("createdAt").toString();
            logger.debug("Timestamp of Doc: "+timestamp);
            if(uploadUtil.timeDiffNow(timestamp)>=1)
            {
                String path = (String) queryFile.get("path");
                deleteFromStorageByLocation(path);
                if(reference.delete().isDone())
                {
                    logger.debug(path+": deleted from firestore!");
                }
            }
            else
            {
                logger.debug(queryFile.get("path")+": skipped, criteria not met");
            }
        }

        logger.debug("Delete Task Completed");

    }

    private void deleteFromStorageByLocation(String path) {
        Bucket bucket = uploadUtil.getBucket();
        bucket.getStorage().delete(BlobId.fromGsUtilUri(GSURL_PREFIX+path));
        logger.debug("Deleted: "+path);
    }
}
