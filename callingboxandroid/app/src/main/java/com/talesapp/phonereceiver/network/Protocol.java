package com.talesapp.phonereceiver.network;

public class Protocol {
	public final static String URL_ROOT = "http://plutos.iptime.org:8009/call_rec";
	
	public final static String URL_GET_CONTACT = URL_ROOT + "/view_json.jsp?";
	public final static String URL_UPLOAD_CONTACT = URL_ROOT + "/insert_app.jsp";
	
	public final static String PARAM_GET_CONTACT = "v_cell_no=";
}
