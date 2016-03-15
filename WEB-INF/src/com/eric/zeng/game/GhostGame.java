package com.eric.zeng.game;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class GhostGame extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_tomcatRootDir = config.getServletContext().getRealPath(config.getServletContext().getContextPath());	
	}
 
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {

		String currentWord = request.getParameter("word").toString();
		ComputerWordGenerator cg = null;
		JSONObject json = new JSONObject();
		try{		
			try{
				cg = new ComputerWordGeneratorImpl(_tomcatRootDir,currentWord);
			}catch(WordListFileCannotBeLoadedException e){
				json.put(WORDTAG,e.getErrormsg());
				json.put(GAMEOVERTAG, true);
			}
			if(cg == null){
				json.put(WORDTAG,"System Error.");
				json.put(GAMEOVERTAG, true);
			}
			
			if(cg.checkUserLost(currentWord)!=GameConstant.eOK){
				json.put(WORDTAG,"You lost! " + currentWord +" is a word or could not continue.");
				json.put(GAMEOVERTAG, true);
				 
			}else{
				int status = cg.generateNextCharacter();
				if(status == GameConstant.eOK){
					json.put(WORDTAG,cg.getComputerReturnedString() );
					json.put(GAMEOVERTAG, false);
				}
				if(status == GameConstant.CPU_LOST_CODE){
					json.put(WORDTAG,"You Win! CPU return a word " + cg.getComputerReturnedString() );
					json.put(GAMEOVERTAG, true);
				}
			}
		}catch (JSONException jse)
		{ 
			//TODO handle exception
		    response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("Json Error");
		}
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	private static String _tomcatRootDir;
	private final String WORDTAG = "retword";
	private final String GAMEOVERTAG = "gameover";
}
