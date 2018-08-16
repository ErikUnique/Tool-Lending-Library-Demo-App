package com.techelevator.controller;

import java.io.Console;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techelevator.model.dao.ReservationDAO;

@Controller
public class ReservationController {

	@Autowired
	private ReservationDAO reservationDAO;
	
	@RequestMapping(path = "/toolHistory", method = RequestMethod.POST)
	public String displayToolHistory(HttpServletRequest request) {
		
		//defaults to false unless found to be true later on
		request.setAttribute("charError", false);
		
		if (request.getParameter("searchString") != null && !request.getParameter("searchString").isEmpty()) {
			
			// This code tests if the searchString only contains numbers
			boolean onlyHasNums = true;
			char[] a = request.getParameter("searchString").toCharArray();
			for (char c: a)
			{
				onlyHasNums = 
			            ((c >= '0') && (c <= '9'));

			    if (!onlyHasNums)
			    {
			        break;
			    }
			}
			
			
			//This code tests if the searchString only contains letters
			boolean onlyHasLetters = true;
			a = request.getParameter("searchString").toCharArray();
			for (char c: a)
			{
				onlyHasLetters = 
						((c >= 'a') && (c <= 'z')) || 
			            ((c >= 'A') && (c <= 'Z'));

			    if (!onlyHasLetters)
			    {
			        break;
			    }
			}
		
			
			//This code tests if the string only contains either numbers or letters (no other character types)
			boolean onlyHasNumsAndLetters = true;
			a = request.getParameter("searchString").toCharArray();
			for (char c: a)
			{
				onlyHasNumsAndLetters = ((c >= 'a') && (c <= 'z')) || 
			            ((c >= 'A') && (c <= 'Z')) || 
			            ((c >= '0') && (c <= '9'));

			    if (!onlyHasNumsAndLetters)
			    {
			        break;
			    }
			}
			
	
			// This chunk sets an error message and error boolean and returns to the jsp, if the string contains the wrong characters
			if(request.getParameter("searchType").equals("driversLicense") && !onlyHasNumsAndLetters)
			{ request.setAttribute("charErrorMsg", "Please only enter numbers and letters for a driver's license search");
			request.setAttribute("charError", true);
			return "toolHistory";
			}
			if( request.getParameter("searchType").equals("toolId") && !onlyHasNums) {
				request.setAttribute("charErrorMsg", "Please only enter numbers for a tool ID search");
				request.setAttribute("charError", true);
				return "toolHistory";
			}
			if(request.getParameter("searchType").equals("userName") && !onlyHasLetters ){
				request.setAttribute("charErrorMsg", "Please only enter letters for a name search");
				request.setAttribute("charError", true);
				return "toolHistory";
			}
				

			
			// This junk actually calls the SQL statements if it's a valid search string
			if (request.getParameter("searchType").equals("driversLicense")) {
				request.setAttribute("reservedTools",
						reservationDAO.searchToolsByDriversLicense(request.getParameter("searchString")));
			}
			if (request.getParameter("searchType").equals("toolId")) {
				request.setAttribute("reservedTools",
						reservationDAO.searchToolsByToolNumber(Integer.parseInt(request.getParameter("searchString"))));
			}
			if (request.getParameter("searchType").equals("userName")) {
				request.setAttribute("reservedTools",
						reservationDAO.searchToolsByName(request.getParameter("searchString")));
			}
		}
		return "toolHistory";
	}

	
	
	@RequestMapping(path="/toolHistory", method = RequestMethod.GET)
	public String displayToolHistoryFirst(HttpServletRequest request) {
		return "toolHistory";
	}

	@RequestMapping("/checkedOutTools")
	public String displayCheckedOutTools(HttpServletRequest request) {

		request.setAttribute("allCheckedOutTools", reservationDAO.getAllCheckedOutTools());

		return "checkedOutTools";
	}
	
}
