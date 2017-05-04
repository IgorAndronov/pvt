package com.pvt.web.utils;


import com.pvt.web.LoginController;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.pvt.web.utils.WebConstants.*;



/**
 * Filter checks if LoginBean has loginIn property set to true.
 * If it is not set then request is being redirected to the login.xhml page.
 *
 *
 */
public class LoginFilter implements Filter {

    /**
     * Checks if user is logged in. If not it redirects to the login.xhtml page.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Filter works");
        // Get the loginBean from session attribute
        LoginController loginController = (LoginController)((HttpServletRequest)request).getSession().getAttribute(LOGIN_CONTROLLER);

        // For the first application request there is no loginBean in the session so user needs to log in
        // For other requests loginBean is present but we need to check if user has logged in successfully
        //((HttpServletResponse)response).sendRedirect("login.jsf");

        if (loginController == null || !loginController.isLoggedIn()) {

            String contextPath = ((HttpServletRequest)request).getContextPath();
            System.out.println("contextPath="+contextPath);


            //JSF ajax requests expect XML responses with HTTP status 200.
            // If you send a synchronous redirect, then a HTTP status 302 response will be sent which
            // will be completely ignored by JSF ajax engine. You should instead be sending a normal
            // HTTP 200 response with a specific piece of XML which tells the JSF ajax engine to perform a redirect.
            // Do this instead of httpServletResponse.sendRedirect()

            if ("partial/ajax".equals( ((HttpServletRequest)request).getHeader("Faces-Request")) ) {
                System.out.println("partial ajax");

                ((HttpServletResponse)response).setContentType("text/xml");
                ((HttpServletResponse)response).getWriter()
                        .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                        .printf("<partial-response><redirect url=\"%s\"></redirect></partial-response>", contextPath + PUBLIC_PAGES_PATH);
            }
            else {
                System.out.println("full request");
                ((HttpServletResponse)response).sendRedirect(contextPath +"/"+ LOGIN_CONTROLLER );
            }


        }else{

            System.out.println("chain is ="+chain.toString()+ " name="+chain.getClass().getCanonicalName());

            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        // Nothing to do here!
    }

    public void destroy() {
        // Nothing to do here!
    }

}