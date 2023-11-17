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
@ToString
@Builder
public class Post implements Entity {
    private Long id;
    private Status status;
    private String title;
    private String content;
    private List<Label> labels;

    @Override
    public String toStringTableViewWithIds() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = getColumnDataWithIds();
        return "\n" + AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    @Override
    public String toStringTableViewEntityNoIds() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = getColumnDataNoIds();
        return "\n" + AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    public static List<ColumnData<Post>> getColumnDataWithIds() {
        return Arrays.asList(
                new Column().header("ID")
                        .headerAlign(HorizontalAlign.CENTER)
                        .dataAlign(HorizontalAlign.CENTER)
                        .with(post -> String.valueOf(post.getId())),
                new Column().header("Title")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Post::getTitle),
                new Column().header("Content")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Post::getContent),
                new Column().header("Labels (Name(id))")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> post.getLabels().stream()
                                .map(label -> label.getName() + "(" + label.getId().toString()+")")
                                .collect(Collectors.joining(", ")))
        );
    }

    private List<ColumnData<Post>> getColumnDataNoIds() {
        return Arrays.asList(
                new Column().header("Title")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Post::getTitle),
                new Column().header("Content")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Post::getContent),
                new Column().header("Labels (name)")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> post.getLabels().stream()
                                .map(Label::getName)
                                .collect(Collectors.joining(", ")))
        );
    }

}
