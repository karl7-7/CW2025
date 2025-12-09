package com.comp2042.logic.game;

/**
 * Immutable data transfer object returned after a DOWN/HARD_DROP move.
 *
 * <p>Contains optional {@link ClearRow} information (null when no rows were cleared)
 * and the {@link ViewData} snapshot that the UI uses to refresh visuals.
 */

public final class DownData { //an immutable class used to return multiple pieces of info after a brick moves down
    private final ClearRow clearRow; //information about cleared rows
    private final ViewData viewData; //information needed by the GUI to update brick's position and shape

    /**
     * Create a DownData bundle.
     *
     * @param clearRow information about cleared rows (may be null)
     * @param viewData view snapshot for rendering the active brick
     */

    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Get clearing information for this move.
     *
     * @return ClearRow instance or null
     */

    public ClearRow getClearRow() {
        return clearRow; //return the cleared row information
    }

    /**
     * Get the view snapshot produced after the move.
     *
     * @return ViewData for rendering
     */

    public ViewData getViewData() {
        return viewData; //return the current view information for rendering the brick
    }
}
