package cz.muni.fi.pv286.mmt.model;

/**
 * Regular io option enum.
 */
public enum IoOption {
    BIG,
    LITTLE,
    LEFT,
    RIGHT,
    HEX,
    DECIMAL,
    CHARACTER,
    BINARY;

    public static IoOption from(FromToOption fromToOption) {
        return IoOption.valueOf(fromToOption.name());
    }
}
