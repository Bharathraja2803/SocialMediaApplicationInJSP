package org.bharath.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bharath.dao.UsersDaoImpl;
import org.bharath.model.Users;
import org.bharath.utils.MainCentralizedResource;


public class CreateUserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Users users = new Users();
		users.setUserName_(request.getParameter("username"));
		users.setPassword_(MainCentralizedResource.generateHashedPassword(request.getParameter("password")));
		users.setBirthday_(LocalDate.parse(request.getParameter("dob"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		users.setEmailId_(request.getParameter("mailid"));
		UsersDaoImpl usersDaoImpl = UsersDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		int userid = usersDaoImpl.addNewUser(users);
		
		HttpSession session = request.getSession();
		if(userid == -1){
			session.setAttribute("isAccountNotCreated", "n");
			RequestDispatcher rd = request.getRequestDispatcher("signupPage.jsp");
			rd.forward(request, response);
			return;
		}
		
		session.setAttribute("newUserCreatedId", userid);
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

}
