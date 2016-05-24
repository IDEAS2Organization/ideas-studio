<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<header id="header">
    <div id="headerDiv" class="row-fluid">
        <!--<h1><a href="index.jsp">STATService</a></h1>-->
        <div id="logo" class="span4">
            <a href="http://moses.us.es/e3"><img src="./img/E3-Logo.png" alt="e3" /></a>
            <div id="moses-logo">
                <spring:message code="master.page.is-part-of" /> 
                <a href="http://moses.us.es"><img src="./img/MOSES-Logo-verySMALL.png" alt="MOSES"/></a>    
            </div>
        </div>        

        <div id="logosRG" class="span4">
            <a href="http://www.us.es"><img src="./img/USLogo.png" alt="University of Sevilla"/></a>
            <!--<a href="http://www.lsi.us.es"><img src="./imgs/LSI.gif" alt="Department of Computing Languages and Systems"/></a>-->
            <a href="http://www.isa.us.es"  target="_blank" ><img src="./img/ISALogo.png" alt="Applied Software Engineering Research Group"/></a>            
        </div>


        <div id="login" class="pull-right span3">

            <security:authorize access="isAnonymous()">
                <a class="btn" href="security/login">
                    <i class="icon-user"></i> 
                    <spring:message code="master.page.login" />
                </a>
                <div id="socialSinginButtons" class="form-inline">		
                    <span>Sign in  with:</span>
                    <table><tr><td>
                                <form id="fa_signin" action="signin/facebook" method="POST" class="form-horizontal">
                                    <input type="hidden" name="scope" value="email">
                                    <button type="submit" class="btn btn-social btn-xs btn-facebook"> 
                                        <i class="fa fa-facebook"></i> Facebook 
                                    </button> 
                                </form>
                            </td>
                            <td>
                                <form id="tw_signin" action="signin/twitter" method="POST" class="form-horizontal">                                        
                                    <button type="submit" class="btn btn-social btn-xs btn-twitter">
                                        <i class="fa fa-twitter"></i> Twitter
                                    </button>
                                </form>
                            </td>
                            <td>
                                <form id="go_signin" action="signin/google" method="POST" class="form-inline">
                                    <input type="hidden" name="scope" value="https://www.googleapis.com/auth/userinfo.email">					
                                    <button type="submit" class="btn btn-social btn-xs btn-google-plus" href="/signin/google">
                                        <i class="fa fa-google-plus"></i> Google
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </table>
                </div>
                <a class="btn btn-success" href="researcher/create">
                    <spring:message code="master.page.register" />
                </a>
            </security:authorize>                        
            <security:authorize access="isAuthenticated()">
                <i class="icon-user"></i>
                <a href="researcher/edit"> 
                    <security:authentication property="principal.username" />                                             
                </a>
                <a href="j_spring_security_logout" class="btn-small btn-danger">
                    <i class="icon-remove-circle"></i>
                    <spring:message code="master.page.logout" /> 
                </a>
                <!--<div id="facebook-conectivity">
                    <a href='<c:url value="/connect/facebook" />'>Connect to Facebook</a>
                </div>
                <div id="twitter-contectivity">
                    <a href='<c:url value="/connect/twitter" />'>Connect to Twitter</a>
                </div>                
                <div id="linkedin-conectivity">
                    <a href='<c:url value="/connect/linkedin" />'>Connect to Linkedin</a>
                </div>-->
            </security:authorize>        
        </div>
        <div id="language" class="pull-right span3">
            <a href="?language=en">en</a> | <a href="?language=es">es</a>
        </div>    
    </div>
</header>