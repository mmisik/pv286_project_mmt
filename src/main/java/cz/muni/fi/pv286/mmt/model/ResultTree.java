package cz.muni.fi.pv286.mmt.model;

import java.util.List;

public class ResultTree {
    private byte[] value;
    private List<ResultTree> children;

    public ResultTree(byte[] value) {
        this.value = value;
    }

    public ResultTree(List<ResultTree> children) {
        this.children = children;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public List<ResultTree> getChildren() {
        return children;
    }

    public void setChildren(List<ResultTree> children) {
        this.children = children;
    }
}
