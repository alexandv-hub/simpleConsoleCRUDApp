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
public class Post extends BaseEntity<Long> {
    private String title;
    private String content;
    private List<Label> labels;

    public Post(Long id, Status status, String title, String content, List<Label> labels) {
        super(id, status);
        this.title = title;
        this.content = content;
        this.labels = labels;
    }

    @Override
    public String toStringEntityTableView() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = getColumnData();
        return AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    public static List<ColumnData<Post>> getColumnData() {
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
                new Column().header("Labels (name(id))")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> post.getLabels().stream()
                                .map(label -> label.getName() + "(" + label.getId().toString()+")")
                                .collect(Collectors.joining(", ")))
        );
    }
}
