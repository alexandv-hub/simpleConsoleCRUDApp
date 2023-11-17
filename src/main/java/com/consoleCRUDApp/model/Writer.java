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
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@ToString
public class Writer implements Entity {
    private Long id;
    private Status status;
    private String firstName;
    private String lastName;
    private List<Post> posts;

    @Override
    public String toStringTableViewWithIds() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Writer>> columns = getColumnDataWithIds();
        return "\n" + AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    @Override
    public String toStringTableViewEntityNoIds() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Writer>> columns = getColumnDataNoIds();
        return "\n" + AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    public static List<ColumnData<Writer>> getColumnDataWithIds() {
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
                new Column().header("Posts (Title(ID))")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(writer -> writer.getPosts().stream()
                                .map(post -> post.getTitle() + "(" + post.getId().toString() + ")")
                                .collect(Collectors.joining(", ")))
        );
    }

    private List<ColumnData<Writer>> getColumnDataNoIds() {
        return Arrays.asList(
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
                new Column().header("Posts (Title)")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(writer -> writer.getPosts().stream()
                                .map(Post::getTitle)
                                .collect(Collectors.joining(", ")))
        );
    }


}
