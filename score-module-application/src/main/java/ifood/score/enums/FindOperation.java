package ifood.score.enums;

import java.util.HashMap;
import java.util.Map;

public enum FindOperation {	
	ABOVE("above"), BELOW("below");	
	
	private static Map<String, FindOperation> map = new HashMap<>();
	
	static {
		for (FindOperation e : FindOperation.values()) {
			map.put(e.type, e);
		}
	}
	
	private String type;
	
	FindOperation (String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public static FindOperation getEnum(String type) {
		return map.get(type);
	}
}
