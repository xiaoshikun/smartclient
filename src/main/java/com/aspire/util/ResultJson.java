package com.aspire.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;

/**
 *返回JSON数据格式
 * @author 萧杰
 *
 */
public class ResultJson implements Serializable{

	// 定义jackson对象
	private static final ObjectMapper MAPPER = new ObjectMapper();
     
	// 响应业务状态
    /*
     * 0	成功
     * 1	错误
     */
	private int code;
	// 响应消息
    private String msg;
	// 响应中的数据
    private Object data;

	public static ResultJson build(Integer code, String msg, Object data) {
		return new ResultJson(code, msg, data);
	}

	public static ResultJson oK(Object data) {
		return new ResultJson(data);
	}

	public static ResultJson oK() {
		return new ResultJson(null);
	}

	public ResultJson() {

	}

	public static ResultJson build(Integer code, String msg) {
		return new ResultJson(code, msg, null);
	}

	public ResultJson(Integer code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ResultJson(Object data) {
		this.code = 0;
		this.msg = "OK";
		this.data = data;
	}


	public Boolean isOk() {
		return this.code == 0;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 将json结果集转化为SysResult对象
	 *
	 * @param jsonData json数据
	 * @param clazz SysResult中的object类型
	 * @return
	 */
	public static ResultJson formatToPojo(String jsonData, Class<?> clazz) {
		try {
			if (clazz == null) {
				return MAPPER.readValue(jsonData, ResultJson.class);
			}
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			JsonNode data = jsonNode.get("data");
			Object obj = null;
			if (clazz != null) {
				if (data.isObject()) {
					obj = MAPPER.readValue(data.traverse(), clazz);
				} else if (data.isTextual()) {
					obj = MAPPER.readValue(data.asText(), clazz);
				}
			}
			return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 没有object对象的转化
	 *
	 * @param json
	 * @return
	 */
	public static ResultJson format(String json) {
		try {
			return MAPPER.readValue(json, ResultJson.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Object是集合转化
	 *
	 * @param jsonData json数据
	 * @param clazz 集合中的类型
	 * @return
	 */
	public static ResultJson formatToList(String jsonData, Class<?> clazz) {
		try {
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			JsonNode data = jsonNode.get("data");
			Object obj = null;
			if (data.isArray() && data.size() > 0) {
				obj = MAPPER.readValue(data.traverse(),
						MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
			}
			return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
