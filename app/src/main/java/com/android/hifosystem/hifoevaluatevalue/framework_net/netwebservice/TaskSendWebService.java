package com.android.hifosystem.hifoevaluatevalue.framework_net.netwebservice;

import android.content.Context;

import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class TaskSendWebService {

	public Context context;
	public static final String NAME_SPACE = "http://tempuri.org/";
	public static String getWebServiceData(String endPoint , String methodName,
										   List<Map<String, Object>> par) {
		if (methodName.length() <= 0)
			return null;
		String result = null;
		LogUtil.log("start netData:"+methodName);
		String soapAction = NAME_SPACE + methodName;
		if (par != null) {
			String values = "";
			for (int i = 0; i < par.size(); i++) {
				values += (String)par.get(i).get("name") + "="+ par.get(i).get("value") + "&";
				LogUtil.log("name:"+(String)par.get(i).get("name")+"  value:" +par.get(i).get("value"));
			}
			LogUtil.log("url", endPoint + "/"+ methodName + "?" + values);
		}

		//如果缓存存在的话就会从缓冲中区取。
//		if (isCache) {
//			if ((result = readCache(methodName, par)) != null) {
//				if(result.length() > 0)
//				{
//					LogUtil.log(methodName+" return from cache:"+result);
//					return result;
//				}
//			}
//		}
		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(NAME_SPACE, methodName);
		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		if (par != null) {
			for (int i = 0; i < par.size(); i++) {
				rpc.addProperty((String)par.get(i).get("name"), par.get(i).get("value"));
			}
		}
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
//			envelope.setOutputSoapObject(rpc);
		(new MarshalBase64()).register(envelope);
		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// 调用WebService
			transport.call(soapAction, envelope);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		// 获取返回的数据
		SoapObject object = (SoapObject) envelope.bodyIn;
		// 获取返回的结果
		try{
			result = object.getProperty(0).toString();
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			return  "";
		}
		LogUtil.log(methodName+"return from net:"+result);

//		if (isCache && result.length() > 10) {
//			writeCache(methodName, par, result);
//		}
		return result;
	}

//	public static String readCache(String methodName, List<Map<String, Object>> par) {
//		String result = null;
//		String fileName = methodName;
//		if (par != null) {
//			for (int i = 0; i < par.size(); i++) {
//				fileName += par.get(i).get("value");
//			}
//		}
//		fileName += ".js";
//		String cacheHome = StoragePath.cacheDir + "/" + methodName;
//		String filePath = cacheHome + "/" + fileName;
//		result = FileOpt.readFileData(filePath);
//		LogUtil.log("cache fileName:"+filePath);
//		return result;
//	}

	/**
	 * 功能 ： 将数据保存到缓存中去。
	 * 传递参数 ：
	 * 返回类型 ：void
	 * 作者 ： wanny
	 * 时间 ：2015年6月18日上午10:23:07
	 */
//	private static void writeCache(String methodName,
//								   List<Map<String, Object>> par, String data) {
//		if (!StoragePath.isSdExists())
//			return;
//		StoragePath.createDirs();
//		String fileName = methodName;
//		if (par != null) {
//			for (int i = 0; i < par.size(); i++) {
//				fileName += par.get(i).get("value");
//			}
//		}
//		fileName += ".js";
//		String cacheHome = StoragePath.cacheDir + "/" + methodName;
//		File file = new File(cacheHome);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//		FileOpt.writeFileData(file.getAbsolutePath(), fileName, data);
//	}

	public static String getParam(Map<String, String> params) {
		String param = "";
		if (params != null) {
			Iterator<String> it = params.keySet().iterator();
			while (it.hasNext()) {
				String str = it.next();
				param += "<" + str + ">";
				param += params.get(str);
				param += "</" + str + ">";
			}
		}
		return param;
	}


//		public Context context;
//        public static final  String NAME_SPACE = "http://tempuri.org/";
//		public static String getWebServiceData(String endPoint ,String methodName,
//				List<Map<String, Object>> par, boolean isCache) {
//			if (methodName.length() <= 0)
//				return null;
//			String result = null;
//			LogUtil.log("start netData:"+methodName);
//			//发布任务的地址
////			String endPoint = "http://192.168.4.253:8089/TaskWebService.asmx";
//			//测试环境
////	 	    String endPoint = "http://222.178.89.154:59999/javabizservice.asmx";
//			// SOAP Action
//			String soapAction = NAME_SPACE + methodName;
//			if (par != null) {
//				String values = "";
//				for (int i = 0; i < par.size(); i++) {
//					values += (String)par.get(i).get("name") + "="+ par.get(i).get("value") + "&";
//					LogUtil.log("name:"+(String)par.get(i).get("name")+"  value:" +par.get(i).get("value"));
//				}
//				LogUtil.log("url", endPoint + "/"+ methodName + "?" + values);
//			}
//
//			//如果缓存存在的话就会从缓冲中区取。
//			if (isCache) {
//				if ((result = readCache(methodName, par)) != null) {
//					if(result.length() > 0)
//					{
//						LogUtil.log(methodName+" return from cache:"+result);
//						return result;
//					}
//				}
//			}
//
////			Element[] header = null;
////			if (!methodName.equals(JsosDataAnalyze.LoginInterface)) {
////				header = new Element[icon_switch_thumb];
////				header[0] = new Element().createElement(NAME_SPACE,
////						"CertficateSoapHeader");
////				//添加token
////				Element token = new Element().createElement(NAME_SPACE,
////						"Token");
////				if(StaticVal.loginUserData.getToken() != null && !(StaticVal.loginUserData.getToken()).equals("")){
////					 token.addChild(Node.TEXT,  StaticVal.loginUserData.getToken());
////					 header[0].addChild(Node.ELEMENT, token);
////				}else{
//////					 return "登录过时,请重新登录";
////					 token.addChild(Node.TEXT, "");
////					 header[0].addChild(Node.ELEMENT, token);
////				}
////				//token.addChild(Node.TEXT,"hfshfs2854515");
////				//添加userid
////				Element userid = new Element().createElement(NAME_SPACE, "UserID");
////				if(StaticVal.loginUserData.getUserId() != null && !(StaticVal.loginUserData.getUserId()).equals("")){
////					userid.addChild(Node.TEXT, StaticVal.loginUserData.getUserId());
////					 header[0].addChild(Node.ELEMENT, userid);
////				}else{
//////					 return "登录过时,请重新登录";
////					 userid.addChild(Node.TEXT, "");
////					 header[0].addChild(Node.ELEMENT, userid);
////				}
//////				userid.addChild(Node.TEXT,StaticVal.loginUserData.getUserId());
//////				header[0].addChild( Node.ELEMENT,userid);
////
////				//添加手机设备的id
////				Element clientId = new Element().createElement(NAME_SPACE, "ClientName");
////				if(StaticVal.loginUserData.getClientId() != null && !(StaticVal.loginUserData.getClientId()).equals("")){
////					clientId.addChild(Node.TEXT, StaticVal.loginUserData.getClientId());
////					 header[0].addChild(Node.ELEMENT, clientId);
////				}else{
//////					 return "登录过时,请重新登录";
////					 clientId.addChild(Node.TEXT, "");
////					 header[0].addChild(Node.ELEMENT, clientId);
////				}
////	            //clientId.addChild(Node.TEXT,StaticVal.loginUserData.getClientId());
////	            //header[0].addChild( Node.ELEMENT,clientId);
////				//设置参数
////	     		//header[0].setAttribute(NAME_SPACE, "Token", StaticVal.loginUserData.getToken());
////			}
//			// 指定WebService的命名空间和调用的方法名
//			SoapObject rpc =   new SoapObject(NAME_SPACE, methodName);
//			//传递的Token是用户的名字
////			if(!methodName.equals(JsosDataAnalyze.LoginInterface)){
////				rpc.addProperty("Token", C.dataSp.getSValue(D.USERID, ""));
////			}
//			// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
//			if (par != null) {
//				for (int i = 0; i < par.size(); i++) {
//					rpc.addProperty((String)par.get(i).get("name"), par.get(i).get("value"));
//				}
//			}
//			// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
//			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//					SoapEnvelope.VER11);
//		 //添加请求头
////			if(header != null)
////			{
////				C.showLog("add header "+methodName);
////				envelope.headerOut = header;
////			}
//			envelope.bodyOut = rpc;
//			// 设置是否调用的是dotNet开发的WebService
//			envelope.dotNet = true;
//			// 等价于envelope.bodyOut = rpc;
////			envelope.setOutputSoapObject(rpc);
//			(new MarshalBase64()).register(envelope);
//			HttpTransportSE transport = new HttpTransportSE(endPoint);
//			try {
//				// 调用WebService
//				transport.call(soapAction, envelope);
//			} catch (Exception e) {
//				e.printStackTrace();
//				return "";
//			}
//			// 获取返回的数据
//		SoapObject  object = (SoapObject ) envelope.bodyIn;
//			// 获取返回的结果
//			try{
//				result = object.getProperty(0).toString();
//
//			}catch(ArrayIndexOutOfBoundsException e){
//				e.printStackTrace();
//			}
//			LogUtil.log(methodName+"return from net:"+result);
//
//			if (isCache && result.length() > 10) {
//				writeCache(methodName, par, result);
//			}
//			return result;
//		}
//
//		public static String readCache(String methodName,
//				List<Map<String, Object>> par) {
//			String result = null;
//			String fileName = methodName;
//			if (par != null) {
//				for (int i = 0; i < par.size(); i++) {
//					fileName += par.get(i).get("value");
//				}
//			}
//			fileName += ".js";
//			String cacheHome = StoragePath.cacheDir + "/" + methodName;
//			String filePath = cacheHome + "/" + fileName;
//			result = FileOpt.readFileData(filePath);
//			LogUtil.log("cache fileName:"+filePath);
//			return result;
//		}
//
//		/**
//		 * 功能 ： 将数据保存到缓存中去。
//		 * 传递参数 ：
//		 * 返回类型 ：void
//		 * 作者 ： wanny
//		 * 时间 ：2015年6月18日上午10:23:07
//		 */
//		private static void writeCache(String methodName,
//				List<Map<String, Object>> par, String data) {
//			if (!StoragePath.isSdExists())
//				return;
//			String fileName = methodName;
//			if (par != null) {
//				for (int i = 0; i < par.size(); i++) {
//					fileName +=par.get(i).get("value");
//				}
//			}
//			fileName += ".js";
//			String cacheHome = StoragePath.cacheDir + "/" + methodName;
//			File file = new File(cacheHome);
//			if (!file.exists()) {
//				file.mkdirs();
//			}
//			FileOpt.writeFileData(file.getAbsolutePath(), fileName, data);
//		}
//
//		public static String getParam(Map<String, String> params) {
//			String param = "";
//			if (params != null) {
//				Iterator<String> it = params.keySet().iterator();
//				while (it.hasNext()) {
//					String str = it.next();
//					param += "<" + str + ">";
//					param += params.get(str);
//					param += "</" + str + ">";
//				}
//			}
//			return param;
//		}

}
