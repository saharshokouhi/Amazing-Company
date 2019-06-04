package com.tradeshift.model;



import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name="node")
public class Node {

//    public Node(Long id, Node parent) {
//        this.id = id;
//        this.parent = parent;
//    }

    private Long id;

    private int leftPos;

    private int rightPos;

    private int height;

    private Node parent;

    private Optional<List<Node>> children = Optional.empty();

    @Id
    @Column(name= "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="leftPos")
    public int getLeftPos() {
        return leftPos;
    }

    public void setLeftPos(int leftPos) {
        this.leftPos = leftPos;
    }
    @Column(name="rightPos")
    public int getRightPos() {
        return rightPos;
    }

    public void setRightPos(int rightPos) {
        this.rightPos = rightPos;
    }

    @Column(name = "height")
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @ManyToOne(cascade = CascadeType.REMOVE)
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }


    @Transient
    public Optional<List<Node>> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = Optional.of(children);
    }
}
