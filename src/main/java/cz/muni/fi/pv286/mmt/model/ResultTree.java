package cz.muni.fi.pv286.mmt.model;

import java.util.ArrayList;
import java.util.List;

public class ResultTree {
    private byte[] value;
    String intermediate;
    private List<ResultTree> children = new ArrayList<>();

    public ResultTree(byte[] value) {
        this.value = value;
    }

    public ResultTree(String intermediate) {
        this.intermediate = intermediate;
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

    public String getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(String intermediate) {
        this.intermediate = intermediate;
    }
}
