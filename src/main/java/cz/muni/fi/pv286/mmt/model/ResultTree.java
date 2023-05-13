package cz.muni.fi.pv286.mmt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ResultTree structure is used for array parser.
 */
public class ResultTree {
    private byte[] value;
    String intermediate;
    private List<ResultTree> children = new ArrayList<>();
    private int childIndex = 0;

    public ResultTree(byte[] value) {
        this.value = value == null ? null : Arrays.copyOf(value, value.length);
    }

    public ResultTree(String intermediate) {
        this.intermediate = intermediate;
    }

    public ResultTree(List<ResultTree> children) {
        Collections.copy(this.children, children);
    }

    public byte[] getValue() {
        return this.value == null ? null : Arrays.copyOf(value, value.length);
    }

    public void setValue(byte[] value) {
        this.value = value == null ? null : Arrays.copyOf(value, value.length);
    }

    public List<ResultTree> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public String getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(String intermediate) {
        this.intermediate = intermediate;
    }

    public int getChildIndex() {
        return childIndex;
    }

    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }
}
