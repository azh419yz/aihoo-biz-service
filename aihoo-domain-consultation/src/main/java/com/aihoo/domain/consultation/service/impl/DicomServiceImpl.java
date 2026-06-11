package com.aihoo.domain.consultation.service.impl;


import com.aihoo.common.JsonResult;
import com.aihoo.domain.consultation.service.DicomService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DicomServiceImpl implements DicomService {

	@Override
	public void uploadMeiQing(String studyId, String bodypart, String examName, String studyModality,
			String checkTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unzip(String studyId) {
		// TODO Auto-generated method stub

	}

	@Override
	public JsonResult mdtDicomList(Map<String, String> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonResult updateZip(Map<String, String> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonResult cancel(Map<String, String> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonResult modality(Map<String, String> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonResult saveDicom(Map<String, String> param) {
		// TODO Auto-generated method stub
		return null;
	}
}