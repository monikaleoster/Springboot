package readingList;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("amazon")
public class AmazonProperties {
	
	private String associateID;

	public String getAssociateID() {
		return associateID;
	}

	public void setAssociateID(String associateID) {
		this.associateID = associateID;
	}
	
	
}
