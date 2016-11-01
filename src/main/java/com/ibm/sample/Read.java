package com.ibm.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/cloudant/read")
public class Read extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	CloudantClient client = CloudantConnectionService.getConnection();	
		JsonObject output = new JsonObject();

		String docId = request.getParameter("id");

		try {
	    	String dbName = "student";
	    	Database db = client.database(dbName, false);
	
	    	db.find(docId);
	    	InputStream is = db.find(docId);
			int i;
			char c;
			String doc = "";
			while((i=is.read())!=-1)
	         {
	            c=(char)i;
	            doc += c;
	         }
			JsonParser parser = new JsonParser();
			output = parser.parse(doc).getAsJsonObject();
	    	
    	} catch(NoDocumentException ex) {
    		output.addProperty("err", "No Database/Document found");
    	}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

}
