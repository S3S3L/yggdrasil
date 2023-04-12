package io.github.s3s3l.yggdrasil.boot.old.component.factory;

import java.lang.reflect.Method;

public class AtomConstructor {

	private String depotName;
	private Method creator;

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public Method getCreator() {
		return creator;
	}

	public void setCreator(Method creator) {
		this.creator = creator;
	}
}
  