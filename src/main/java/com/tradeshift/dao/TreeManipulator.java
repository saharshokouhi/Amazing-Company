package com.tradeshift.dao;


import com.tradeshift.model.Node;

public interface TreeManipulator  {

    void changeParent(Node source, Node destination);

    void saveTree(Node node);
}
