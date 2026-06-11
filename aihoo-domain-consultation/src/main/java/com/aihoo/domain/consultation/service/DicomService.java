package com.aihoo.domain.consultation.service;


import com.aihoo.common.JsonResult;

import java.util.Map;

public interface DicomService {

    void uploadMeiQing(String studyId, String bodypart, String examName, String studyModality, String checkTime);

    void unzip(String studyId);

    JsonResult mdtDicomList(Map<String, String> param);

    JsonResult updateZip(Map<String, String> param);

    JsonResult cancel(Map<String, String> param);

    JsonResult modality(Map<String, String> param);

    JsonResult saveDicom(Map<String, String> param);

}