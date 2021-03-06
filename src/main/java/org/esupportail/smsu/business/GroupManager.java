package org.esupportail.smsu.business;


import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;











import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esupportail.smsu.dao.DaoService;
import org.esupportail.smsu.dao.beans.Account;
import org.esupportail.smsu.dao.beans.CustomizedGroup;
import org.esupportail.smsu.dao.beans.Person;
import org.esupportail.smsu.services.GroupUtils;
import org.esupportail.smsu.services.ldap.LdapUtils;
import org.esupportail.smsu.web.beans.UICustomizedGroup;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Business layer concerning smsu service.
 *
 */
public class GroupManager {
	
	@Autowired private DaoService daoService;
	@Autowired private LdapUtils ldapUtils;
	@Autowired private GroupUtils groupUtils;

	private final Logger logger = Logger.getLogger(getClass());

	/**
	 * @param label
	 * @return Boolean
	 */
	public Boolean existsCustomizedGroupLabel(final String label) {
		CustomizedGroup customizedGroup = daoService.getCustomizedGroupByLabel(label);
		return customizedGroup != null; 
		
	}
	
	/**
	 * @param label
	 * @param id
	 * @return Boolean
	 */
	public Boolean existsCustomizedGroupLabelWithOthersIds(final String label, final Integer id) {
		CustomizedGroup customizedGroup = daoService.getCustomizedGroupByLabelWithOtherId(label, id);
		return customizedGroup != null; 
	}
	
	/**
	 * retrieve all the customized groups defined in smsu database.
	 * @return list
	 */
	public List<UICustomizedGroup> getAllGroups() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieve the smsu roles from the database");
		}
		List<UICustomizedGroup> result = new LinkedList<>();
		for (CustomizedGroup group : daoService.getAllCustomizedGroups()) {
			result.add(convertToUI(group));
		}
		return result;
	}

	public UICustomizedGroup convertToUI(CustomizedGroup group) {
		UICustomizedGroup result = new UICustomizedGroup();
		result.id = group.getId();
		result.label = group.getLabel();
		result.quotaSms = group.getQuotaSms();
		result.maxPerSms = group.getQuotaOrder();
		result.consumedSms = group.getConsumedSms();
		result.role = group.getRole().getName();
		result.account = group.getAccount().getLabel();
		result.supervisors = convertToUI(group.getSupervisors());
		result.labelIsUserId= ldapUtils.mayGetLdapUserByUid(group.getLabel()) != null;
		result.displayName = groupUtils.getGroupDisplayName(group);
		return result;
	}
	
	public Map<String,String> convertToUI(Set<Person> persons) {
		Map<String,String> result = new HashMap<>(); 
		for (Person person : persons) {
			result.put(person.getLogin(), ldapUtils.getUserDisplayName(person));
		}
		return result;
	}

	private CustomizedGroup convertFromUI(final UICustomizedGroup uiCGroup, boolean isAddMode, HttpServletRequest request) {
		CustomizedGroup result = new CustomizedGroup();

		if (!isAddMode) {
			result.setId(uiCGroup.id);
		}		
		result.setLabel(uiCGroup.label.trim());
		result.setConsumedSms(Long.parseLong("0"));
		if (request.isUserInRole("FCTN_GESTION_QUOTAS"))
			result.setQuotaSms(uiCGroup.quotaSms);
		if (request.isUserInRole("FCTN_GESTION_QUOTAS"))
			result.setQuotaOrder(uiCGroup.maxPerSms);
		if (request.isUserInRole("FCTN_GESTION_ROLES_AFFECT"))
			result.setRole(daoService.getRoleByName(uiCGroup.role));
		
		// Manage Account
		String account = uiCGroup.account.trim();
		if (daoService.getAccountByLabel(account) == null) {
			daoService.saveAccount(new Account(account)); 
		}
		result.setAccount(daoService.getAccountByLabel(account));
		
		if (uiCGroup.supervisors != null && request.isUserInRole("FCTN_GESTIONS_RESPONSABLES"))
			result.setSupervisors(convertFromUI(uiCGroup.supervisors));
	
		return result;
	}

	private Set<Person> convertFromUI(Map<String, String> supervisors) {
		Set<Person> personsToAdd = new HashSet<>();
		for (String uip : supervisors.keySet()) {			
			if (daoService.getPersonByLogin(uip) == null) { 
				// add new persons in Person DataBase
				daoService.addPerson(new Person(uip)); 
			}
			personsToAdd.add(daoService.getPersonByLogin(uip));
		}
		return personsToAdd;
	}
		
	public void deleteCustomizedGroup(int id) {
		CustomizedGroup customizedGroup = daoService.getCustomizedGroupById(id);
		logger.info("removing cgroup" + id + " " + customizedGroup.getLabel());
		daoService.deleteCustomizedGroup(customizedGroup);
	}
	
	public void addCustomizedGroup(final UICustomizedGroup uiCGroup, HttpServletRequest request) {
		logger.info("adding cgroup" + uiCGroup.id + " " + uiCGroup.label);
		daoService.addCustomizedGroup(convertFromUI(uiCGroup, true, request));
	}

	public void updateCustomizedGroup(final UICustomizedGroup uiCGroup, HttpServletRequest request) {
		logger.info("modifying cgroup" + uiCGroup.id + " " + uiCGroup.label);
		CustomizedGroup cGroup = convertFromUI(uiCGroup, false, request);
		
		CustomizedGroup persistent = daoService.getCustomizedGroupById(cGroup.getId());
		if (persistent == null) throw new InvalidParameterException("invalid application " + cGroup.getId());

		persistent.setLabel(cGroup.getLabel());
		if (request.isUserInRole("FCTN_GESTION_QUOTAS"))
			persistent.setQuotaSms(cGroup.getQuotaSms());
		if (request.isUserInRole("FCTN_GESTION_QUOTAS"))
			persistent.setQuotaOrder(cGroup.getQuotaOrder());
		persistent.setAccount(cGroup.getAccount());
		if (request.isUserInRole("FCTN_GESTION_ROLES_AFFECT"))
			persistent.setRole(cGroup.getRole());
		if (request.isUserInRole("FCTN_GESTIONS_RESPONSABLES"))
			persistent.setSupervisors(cGroup.getSupervisors());
		
		daoService.updateCustomizedGroup(persistent);
	}
    
}
