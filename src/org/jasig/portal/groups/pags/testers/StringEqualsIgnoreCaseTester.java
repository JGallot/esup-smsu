/* Copyright 2004 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package org.jasig.portal.groups.pags.testers;

import org.springframework.ldap.support.filter.EqualsFilter;
import org.springframework.ldap.support.filter.Filter;

/**
 * Tests an <code>IPerson</code> attribute for String equality 
 * regardless of case and answers true if any of the possibly 
 * multiple values of the attribute equals the test value. 
 * Question: how to handle non-default locales?
 * <p>
 * @author Dan Ellentuck
 * @version $Revision: 34757 $
 */

public class StringEqualsIgnoreCaseTester extends StringTester {

public StringEqualsIgnoreCaseTester(String attribute, String test) {
    super(attribute, test);
}
public boolean test(String att) {
    return att.equalsIgnoreCase(testValue);
}
public Filter getLdapFilter(final String attributeName, final String attributeValue) {
	return new EqualsFilter(attributeName, attributeValue);
}

}
