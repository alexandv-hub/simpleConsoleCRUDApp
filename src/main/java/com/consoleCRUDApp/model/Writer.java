package com.consoleCRUDApp.model;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;
import com.github.freva.asciitable.HorizontalAlign;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Writer extends BaseEntity<Long> {
    private String firstName;
    private String lastName;
    private List<Post> posts;

    public Writer(Long id, Status status, String firstName, String lastName, List<Post> posts) {
        super(id, status);
        this.firstName = firstName;
        this.lastName = lastName;
        this.posts = posts;
    }

    public Writer(Long id, Status status, String firstName, String lastName) {
        super(id, status);
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Writer(Long id, Status status) {
        super(id, status);
    }

    @Override
    public String toStringEntityTableView() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Writer>> columns = getColumnData();
        return AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    public static List<ColumnData<Writer>> getColumnData() {
        return Arrays.asList(
                new Column().header("ID")
                        .headerAlign(HorizontalAlign.CENTER)
                        .dataAlign(HorizontalAlign.CENTER)
                        .with(writer -> String.valueOf(writer.getId())),
                new Column().header("First Name")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Writer::getFirstName),
                new Column().header("Last Name")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Writer::getLastName),
                new Column().header("Posts (IDs)")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(writer -> writer.getPosts().stream()
                                .map(post -> post.getTitle() + "(" + post.getId().toString() + ")")
                                .collect(Collectors.joining(", ")))
        );
    }

}
