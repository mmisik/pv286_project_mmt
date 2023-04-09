package cz.muni.fi.pv286.mmt.model;

/**
 * Enum for bracket types.
 */
public enum BracketType {
    CURLY_BRACKET,
    SQUARE_BRACKET,
    REGULAR_BRACKET;

    public static BracketType from(FromToOption fromToOption) {
        return valueOf(fromToOption.name());
    }
}
