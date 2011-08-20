package com.bugyal.imentor.frontend.client;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserCookie  {

	MentorServiceAsync service = GWT.create(MentorService.class);
	
		public UserCookie(String pEmailId, final String fbSessionId){
			createCookie( pEmailId, fbSessionId);
		}

	public boolean createCookie(String pEmailId, final String fbSessionId) {

		boolean status = false;
		service.getParticipantVOByEmailId(pEmailId,
				new AsyncCallback<ParticipantVO>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Unable to create Cookie");
//						status = false;
					}

					@Override
					public void onSuccess(ParticipantVO result) {

						Cookies.setCookie("fbSId", fbSessionId);
						Cookies.setCookie("uName", result.getName());
						Cookies.setCookie("uHasSubjects", subjectsToString(result
								.getHasSubjects()));
						Cookies.setCookie("uNeedSubjects", subjectsToString(result
								.getNeedSubjects()));
						Cookies.setCookie("uLocation", result
								.getLocationString());
						Cookies.setCookie("uLatitude", ""
								+ result.getLatitude());
						Cookies.setCookie("uLongitude", ""
								+ result.getLongitude());
						Cookies.setCookie("uRadius", "" + result.getRadius());
//						status = true;
					}

				});

		return status;
	}

	public void deleteCookie(String fbSessionId) {
		Cookies.removeCookie("fbSId");
		Cookies.removeCookie("uName");
		Cookies.removeCookie("uHasSubjects");
		Cookies.removeCookie("uNeedSubjects");
		Cookies.removeCookie("uLocation");
		Cookies.removeCookie("uLatitude");
		Cookies.removeCookie("uLongitude");
		Cookies.removeCookie("uRadius");
	}

	public boolean updateCookie(String fbSessionId) {

		return false;
	}

	private String subjectsToString(List<String> subjects) {
		String result = new String();
		for (String s : subjects) {
			result += s;
			result += ",";
		}
		result = result.substring(0, result.length() - 2);
		return result;

	}
	
	private List<String> stringToSubjects(String subString){
		List<String> result = new ArrayList<String>();
		StringTokenizer strToken = new StringTokenizer(subString,",");
		while(strToken.hasMoreTokens()){
			result.add(strToken.nextToken());
		}
		return result;
		
	}
	
	
	public LocationData getCookieLocation() {
		LocationData lData = new LocationData();
		lData.setLatitude(Long.parseLong(Cookies.getCookie("uLatitude")));
		lData.setLongitude(Long.parseLong(Cookies.getCookie("uLongitude")));
		lData.setLocation(Cookies.getCookie("uLocation"));
		lData.setRadius(Integer.parseInt(Cookies.getCookie("uRadius")));
		return lData;
	}

	public void setCookieLocation(LocationData lData) {
		Cookies.setCookie("uLocation", lData.getLocation());
		Cookies.setCookie("uLatitude", ""+lData.getLatitude());
		Cookies.setCookie("uLongitude", ""+ lData.getLongitude());
		Cookies.setCookie("uRadius", "" + lData.getRadius());
	}
	

	public List<String> getCookieHasSubjects() {
		return stringToSubjects(Cookies.getCookie("uHasSubjects"));
	}

	public void setCookieHasSubjects(List<String> l) {
		Cookies.setCookie("uHasSubjects", subjectsToString(l));
	}
	
	public List<String> getCookieNeedSubjects() {
		return stringToSubjects(Cookies.getCookie("uNeedSubjects"));
	}

	public void setCookieNeedSubjects(List<String> l) {
		Cookies.setCookie("uNeedSubjects", subjectsToString(l));
	}  
		
}
