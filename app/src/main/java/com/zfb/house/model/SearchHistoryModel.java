package com.zfb.house.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Snekey on 2016/8/15.
 */
@DatabaseTable(tableName = "search_history")
public class SearchHistoryModel {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "history_name")
    private String historyName;

    @DatabaseField(columnName = "history_id")
    private String historyId;

    public SearchHistoryModel() {
    }

    public SearchHistoryModel(String historyName, String historyId) {
        this.historyName = historyName;
        this.historyId = historyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHistoryName() {
        return historyName;
    }

    public void setHistoryName(String historyName) {
        this.historyName = historyName;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }
}
