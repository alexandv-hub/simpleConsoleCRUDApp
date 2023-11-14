package com.consoleCRUDApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public abstract class BaseEntity<ID> {
    private ID id;
    protected Status status;

    public abstract String toStringEntityTableView();
}
