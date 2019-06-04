package com.tradeshift.service;

import com.tradeshift.dao.NodeRepository;
import com.tradeshift.dao.TreeManipulator;
import com.tradeshift.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class GraphServiceImpl implements GraphService {
    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    TreeManipulator treeManipulator;

    @Override
    public List<Node> getChildren(Long id) {
        nodeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("node not found"));
        return nodeRepository.getChildren(id);
    }

    @Override
    public void changeParent(Long sourceId, Long destinationId) {
        Assert.isTrue(!sourceId.equals(destinationId), "source node and destination node could not be equal");
        Node source = nodeRepository
                .findById(sourceId)
                .orElseThrow(() -> new EntityNotFoundException("source node not found"));
        Node destination = nodeRepository
                .findById(destinationId)
                .orElseThrow(() -> new EntityNotFoundException("destination node not found"));
        Assert.notNull(source.getParent(), "root node cant't move");
        treeManipulator.changeParent(source, destination);
    }

    @Transactional
    @Override
    public void insertNew(Node rootNode) {
        validateTree(rootNode, new HashSet<>());
        prepareTree(rootNode, 1, 0);
        nodeRepository.deleteTree();
        treeManipulator.saveTree(rootNode);
    }

    private int prepareTree(Node node, int position, int height) {
        node.setLeftPos(position);
        if (!node.getChildren().isPresent() || node.getChildren().get().isEmpty()) {
            node.setHeight(height);
            node.setRightPos(++position);
            return position;
        }

        node.setHeight(height++);
        List<Node> children = node.getChildren().orElse(new ArrayList<>());
        for (Node child : children) {
            child.setParent(node);
            position = prepareTree(child, ++position, height);
        }
        node.setRightPos(++position);
        return position;
    }

    private void validateTree(Node node, HashSet<Long> ids) {
        Assert.isTrue(!ids.contains(node.getId()), String.format("duplicate id:%d is found.", node.getId()));
        ids.add(node.getId());
        node.getChildren().orElse(new ArrayList<>()).forEach(child -> validateTree(child, ids));
    }


}
