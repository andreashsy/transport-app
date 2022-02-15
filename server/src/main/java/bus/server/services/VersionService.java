package bus.server.services;

import org.springframework.stereotype.Service;

@Service
public class VersionService {
    private String version = "v0.01";

    public String getVersion() {return this.version;}
    public void setVersion(String version) {this.version = version;}
    
}
