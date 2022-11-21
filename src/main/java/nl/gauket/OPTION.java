package nl.gauket;

public enum OPTION {
    // Option 1: pick new partner based on 1 random index and accept or reject
    ACCEPTREJECT(1)
    // Option 2: pick new partners based on 1 random index
    // ,RANDOMSELECT(2)
    ;

    public final int value;

    OPTION(int option) {
        this.value = option;
    }
}
