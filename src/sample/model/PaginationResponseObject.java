package sample.model;

import javafx.collections.ObservableList;

public class PaginationResponseObject {
    private ObservableList list;
    private int totalCount;

    public ObservableList getList() {
        return list;
    }

    public void setList(ObservableList list) {
        this.list = list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
