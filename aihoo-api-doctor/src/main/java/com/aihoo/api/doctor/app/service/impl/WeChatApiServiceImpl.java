package com.aihoo.api.doctor.app.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aihoo.properties.WechatProperties;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aihoo.api.doctor.app.service.WeChatApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信API服务
 */
@Slf4j
@Service
public class WeChatApiServiceImpl implements WeChatApiService {

	@Autowired
	private WechatProperties wechatProperties;

	/**
	 * 获取Access Token 注意：生产环境建议使用Redis缓存，避免频繁调用
	 */
	public String getAccessToken() {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet httpGet = new HttpGet(getAccessTokenUrl());

			HttpResponse response = httpClient.execute(httpGet);
			String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

			JSONObject jsonObject = JSON.parseObject(result);

			Integer errcode = jsonObject.getInteger("errcode");
			if (errcode != null && errcode != 0) {
				throw new RuntimeException("获取Access Token失败：" + jsonObject.getString("errmsg"));
			}

			String accessToken = jsonObject.getString("access_token");
			Integer expiresIn = jsonObject.getInteger("expires_in");

			log.info("获取Access Token成功，有效期：{}秒", expiresIn);

			return accessToken;

		} catch (IOException e) {
			log.error("获取Access Token异常", e);
			throw new RuntimeException("获取Access Token失败：" + e.getMessage());
		}
	}

	/**
	 * 生成小程序码
	 *
	 * @param accessToken 访问令牌
	 * @param requestData 请求参数
	 * @return 小程序码的Base64编码
	 */
	public String generateWxaCode(String accessToken, JSONObject requestData) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(getWxaCodeUrl(accessToken));
			httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
			httpPost.setEntity(new StringEntity(requestData.toJSONString(), StandardCharsets.UTF_8));

			HttpResponse response = httpClient.execute(httpPost);

			// 检查响应类型
			String contentType = response.getEntity().getContentType().getValue();

			// 如果是JSON类型，说明返回了错误信息
			if (contentType.contains("application/json")) {
				String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				JSONObject jsonObject = JSON.parseObject(result);

				Integer errcode = jsonObject.getInteger("errcode");
				String errmsg = jsonObject.getString("errmsg");
				throw new RuntimeException("生成小程序码失败：" + errcode + " - " + errmsg);
			}

			// 如果是图片类型，转换为Base64
			byte[] imageData = toByteArray(response.getEntity().getContent());
			String base64Image = Base64.getEncoder().encodeToString(imageData);

			log.info("小程序码生成成功，大小：{}字节", imageData.length);
			return base64Image;

		} catch (IOException e) {
			log.error("生成小程序码异常", e);
			throw new RuntimeException("生成小程序码失败：" + e.getMessage());
		}
	}

	/**
	 * 将InputStream转换为byte数组
	 */
	private byte[] toByteArray(InputStream inputStream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] data = new byte[4096];
		int bytesRead;
		while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, bytesRead);
		}
		return buffer.toByteArray();
	}

	/**
	 * 获取Access Token接口地址
	 */
	private String getAccessTokenUrl() {
		return "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wechatProperties.getAppId() + "&secret="
				+ wechatProperties.getSecret();
	}

	/**
	 * 获取小程序码接口地址
	 */
	private String getWxaCodeUrl(String accessToken) {
		return "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
	}

}
