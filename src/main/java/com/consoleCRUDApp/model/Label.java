package com.consoleCRUDApp.model;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;
import com.github.freva.asciitable.HorizontalAlign;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Label extends BaseEntity<Long> {
    private String name;

    public Label(Long id, Status status, String name) {
        super(id, status);
        this.name = name;
    }

    @Override
    public String toStringEntityTableView() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Label>> columns = getColumnData();
        return AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    public static List<ColumnData<Label>> getColumnData() {
        return Arrays.asList(
                new Column().header("ID")
                        .headerAlign(HorizontalAlign.CENTER)
                        .dataAlign(HorizontalAlign.CENTER)
                        .with(label -> String.valueOf(label.getId())),
                new Column().header("Name")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Label::getName)
        );
    }
}
