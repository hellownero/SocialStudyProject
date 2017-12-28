package com.ndlp.socialstudy.Umfragen;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;



public class UmfragenDataIntoDatabase extends StringRequest{

    private static final String Register_Request_URL = "http://hellownero.de/SocialStudy/PHP-Dateien/UmfragenDataIntoDatabase.php";
    private Map<String, String> params;

    public UmfragenDataIntoDatabase(String umfragethema, String erstelltamDate, String enddate, String endtime, String user, String arraytostring, String anzahlUmfragen, Response.Listener<String> listener) {
        super(Request.Method.POST, Register_Request_URL, listener, null);
        params = new HashMap<>();
        params.put("umfragethema", umfragethema);
        params.put("erstelldatum", erstelltamDate);
        params.put("enddate", enddate);
        params.put("endtime", endtime);
        params.put("user", user);
        params.put("datastring", arraytostring);
        params.put("anzahlumfragen", anzahlUmfragen);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}


