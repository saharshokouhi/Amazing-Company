package com.tradeshift.service;

import com.tradeshift.model.Node;

import java.util.List;

public interface GraphService {

    List<Node> getChildren(Long id);

    void changeParent(Long toMove, Long destination);

    void insertNew(Node rootNode);
}

