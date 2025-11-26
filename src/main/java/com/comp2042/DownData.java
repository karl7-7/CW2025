package com.comp2042;

public final class DownData { //an immutable class used to return multiple pieces of info after a brick moves down
    private final ClearRow clearRow; //information about cleared rows
    private final ViewData viewData; //information needed by the GUI to update brick's position and shape

    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    public ClearRow getClearRow() {
        return clearRow; //return the cleared row information
    }

    public ViewData getViewData() {
        return viewData; //return the current view information for rendering the brick
    }
}
