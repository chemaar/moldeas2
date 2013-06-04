package org.weso.moldeas.to;
public class StatsPairTO {

	private String userId;
	private String itemId;
	private int hits;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public StatsPairTO(String userId, String itemId) {
		super();
		this.userId = userId;
		this.itemId = itemId;
	}
	public StatsPairTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hits;
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatsPairTO other = (StatsPairTO) obj;
		if (hits != other.hits)
			return false;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "StatsPairTO [userId=" + userId + ", itemId=" + itemId
				+ ", hits=" + hits + "]";
	}
	

	
}
