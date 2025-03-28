package enums;

// File: Genre.java

/**
 * Represents the different genres of books in the library system.
 */
public enum Genre {
    FICTION,
    NON_FICTION,
    SCIENCE,
    HISTORY,
    BIOGRAPHY,
    MYSTERY,
    ROMANCE,
    FANTASY,
    SCIENCE_FICTION,
    HORROR,
    THRILLER,
    POETRY,
    DRAMA,
    CHILDREN,
    YOUNG_ADULT,
    SELF_HELP,
    TRAVEL,
    COOKBOOK,
    ART,
    PHILOSOPHY;

    /**
     * Returns a string representation of the genre, with proper capitalization and spacing.
     *
     * @return A formatted string representation of the genre.
     */
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase().replace('_', ' ');
    }

    /**
     * Converts a string to a Genre enum value, ignoring case and allowing for spaces.
     *
     * @param genreString The string representation of the genre.
     * @return The corresponding Genre enum value.
     * @throws IllegalArgumentException if the input string doesn't match any Genre.
     */
    public static Genre fromString(String genreString) {
        String formattedGenre = genreString.toUpperCase().replace(' ', '_');
        try {
            return Genre.valueOf(formattedGenre);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid genre: " + genreString, e);
        }
    }
}

