package org.example.entity;

public record FileEntity(String name, boolean isDirectory, long size) {

    @Override
    public String toString() {
        return name;
    }
}
