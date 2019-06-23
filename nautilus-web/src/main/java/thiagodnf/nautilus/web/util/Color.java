package thiagodnf.nautilus.web.util;

public enum Color {

    BLUE("#7cb5ec"),
    BLACK("#434348"),
    LIGHT_GREEN("#90ed7d"),
    ORANGE("#f7a35c"),
    PURPLE("#8085e9"),
    PINK("#f15c80"),
    YELLOW("#e4d354"),
    GREEN("#2b908f"),
    RED("#f45b5b"),
    LIGHT_BLUE("#91e8e1");
    
    Color(String hex){
        this.hex = hex;
    }
    
    private final String hex;
    
    public String getHex() {
        return this.hex;
    }
}
