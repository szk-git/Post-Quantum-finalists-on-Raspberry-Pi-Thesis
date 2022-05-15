package com.elte.jfirbj.backend.models.enums;

public enum RoleEnum {
	ROLE_ADMIN("ROLE_ADMIN"),
	ROLE_MODERATOR("ROLE_MODERATOR"),
	ROLE_USER("ROLE_USER");

	public final String label;

	private RoleEnum(String label) {
		this.label = label;
	}

	public static boolean isLabelInEnum(String label) {
		for (RoleEnum e : values()) {
			if (e.label.equals(label)) {
				return true;
			}
		}
		return false;
	}
}
