package org.esupportail.smsu.dao.beans;

import java.io.Serializable;


/**
 * This is an object that contains data related to the supervisor table.
 *
 * @hibernate.class
 *  table="supervisor"
 */
public class Supervisor  implements Serializable {

	/**
	 * Hibernate reference for account.
	 */
	public static final String REF = "Supervisor";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5006528539597549849L;

	/**
	 * customized group.
	 */
	private CustomizedGroup group;

	/**
	 * person that identifies the supervisor.
	 */
	private Person person;

	/**
	 * Bean constructor.
	 */
	public Supervisor() {
		super();
	}

	/**
	 * Constructor for primary key.
	 */
	public Supervisor(
		final CustomizedGroup group,
		final Person person) {

		this.setGroup(group);
		this.setPerson(person);
	}




	/**
     * @hibernate.property
     *  column=CGR_ID
	 * not-null=true
	 */
	public CustomizedGroup getGroup() {
		return this.group;
	}

	/**
	 * Set the value related to the column: CGR_ID.
	 * @param group the CGR_ID value
	 */
	public void setGroup(final CustomizedGroup group) {
		this.group = group;
	}

	/**
     * @hibernate.property
     *  column=PER_ID
	 * not-null=true
	 */
	public Person getPerson() {
		return this.person;
	}

	/**
	 * Set the value related to the column: PER_ID.
	 * @param person the PER_ID value
	 */
	public void setPerson(final Person person) {
		this.person = person;
	}





	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public boolean equals(final Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof Supervisor)) {
			return false;
		} else {
			Supervisor supervisor = (Supervisor) obj;
			if (null != this.getGroup() && null != supervisor.getGroup()) {
				if (!this.getGroup().equals(supervisor.getGroup())) {
					return false;
				}
			} else {
				return false;
			}
			if (null != this.getPerson() && null != supervisor.getPerson()) {
				if (!this.getPerson().equals(supervisor.getPerson())) {
					return false;
				}
			} else {
				return false;
			}
			return true;
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Supervisor#" + hashCode() + "[Customized group=[" + group + "], person=[" + person 
		+  "]]";
	}


}