package es.us.isa.ideas.app.security;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.us.isa.ideas.app.controllers.AbstractController;
import es.us.isa.ideas.app.services.ConfirmationService;

/**
 * 
 * @author japarejo
 */
@Controller
@Transactional
@RequestMapping("/security/useraccount")
public class UserAccountController extends AbstractController {

	@Autowired
	private UserAccountService accountsService;

	@Autowired
	private ConfirmationService confirmationService;

	// Listing -----------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<UserAccount> accounts;
		accounts = accountsService.findAll();
		result = new ModelAndView("security/useraccount/list");
		result.addObject("accounts", accounts);
		result.addObject("requestURI", "security/useraccount/list");
		return result;
	}

	// Creation -----------------------------------------------------------
	@RequestMapping("/create")
	public ModelAndView register() {
		ModelAndView result;
		UserAccount account = new UserAccount();
		result = createEditModelAndView(account);
		return result;
	}

	// Edition ---------------------------------------------------------
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	@ResponseBody
	public UserAccount edit(@PathVariable String id) {
		
		UserAccount user;
		if (id == null)
			user = LoginService.getPrincipal();
		else {
			user = accountsService.findById(Integer.valueOf(id));
			checkAccessToAccount(user);
		}

		Assert.notNull(user);
//		controlAccess(user);
		return user;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public boolean save(@Valid UserAccount userAccount, String oldPass, String newPass) throws Throwable {
		
		if (oldPass.length()>1 && newPass.length()>1) {
			try {
				beginTransaction();
				accountsService.modifyPassword(userAccount, oldPass, newPass);
				commitTransaction();
				return true;
			} catch (Throwable oops) {
				rollbackTransaction();
				System.out.println("account oops");
				throw oops;
//				result = createEditModelAndView(userAccount, oops.getMessage());
			}
		} else {
			accountsService.save(userAccount);
			return true;
		}
		
	}
	
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(UserAccount account, BindingResult binding)
			throws Throwable {
		ModelAndView result;
		try {
			controlAccess(account);
			beginTransaction();
			accountsService.delete(account);
			commitTransaction();
			result = new ModelAndView("redirect:list");
		} catch (Throwable oops) {
			rollbackTransaction();
			result = createEditModelAndView(account, "account.commit.error");
		}
		return result;

	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public ModelAndView resetPassword() {
		return new ModelAndView("security/useraccount/resetPassword");
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ModelAndView resetPassword(String email) {
		ModelAndView result = null;
		try {
			confirmationService.createPasswordResetConfirmation(email);
			result = new ModelAndView("confirmation/confirmationSent");
		} catch (Throwable oops) {
			throw new IllegalArgumentException("No user with email: " + email);
		}
		return result;
	}


	// Ancillary methods -------------------------------------------

	protected ModelAndView createEditModelAndView(UserAccount account) {
		return createEditModelAndView(account, null);
	}

	protected ModelAndView createEditModelAndView(UserAccount account,
			String message) {
		ModelAndView result;
		Collection<Authority> authorities = Authority.listAuthorities();

		result = new ModelAndView("security/useraccount/edit");
		result.addObject("account", account);
		result.addObject("authorities", authorities);
		result.addObject("message", message);
		return result;
	}
	
	protected ModelAndView createGeneralEditModelAndView(UserAccount account) {
		ModelAndView result;

		result = new ModelAndView("security/useraccount/general");
		result.addObject("account", account);
		return result;
	}

	private String controlAccess(UserAccount account) {
		String result = null;
		if (LoginService.getPrincipal().hasAuthority(Authority.ADMIN))
			result = "redirect:list";
		else if (LoginService.getPrincipal().equals(account))
			result = "researcher/dashboard";
		else
			throw new org.springframework.security.access.AuthorizationServiceException(
					"You are not authorized to edit this information!");
		return result;
	}
	
	private void checkAccessToAccount(UserAccount account) {
		if (!LoginService.getPrincipal() .equals(account) || !LoginService.getPrincipal().hasAuthority(Authority.ADMIN)) {
			System.out.println("Trying to access user info without permission.");
			throw new org.springframework.security.access.AuthorizationServiceException("You are not an administrator!");
		}
	}
}
