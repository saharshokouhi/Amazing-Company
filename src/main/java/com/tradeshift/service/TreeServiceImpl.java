package com.tradeshift.service;

import com.tradeshift.dao.NodeRepository;
import com.tradeshift.dao.TreeManipulator;
import com.tradeshift.exception.NodeNotFoundException;
import com.tradeshift.model.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

@Service
public class TreeServiceImpl implements TreeService {
    private static Logger logger = LogManager.getLogger();

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    TreeManipulator treeManipulator;

    /**
     * retrieve all first level children of a node
     *
     * @param id
     * @return list of first level children of node with given id
     * @throws NodeNotFoundException if given id is not exist in DB
     */
    @Override
    public List<Node> getChildren(Long id) {
        nodeRepository.findById(id).orElseThrow(() -> new NodeNotFoundException(String.format("node:%d is not found", id)));
        return nodeRepository.getChildren(id);
    }

    /**
     * change parent of a node with sourceId to node with destinationId
     *
     * @param sourceId
     * @param destinationId
     * @throws NodeNotFoundException  if sourceId or destinationId is not exist in DB
     * @throws IllegalArgumentException if sourceId is root node
     */
    @Override
    public void changeParent(Long sourceId, Long destinationId) {
        Assert.isTrue(!sourceId.equals(destinationId), "source node and destination node could not be equal");
        Node source = nodeRepository
                .findById(sourceId)
                .orElseThrow(() -> new NodeNotFoundException(String.format("source node:%d is not found", sourceId)));
        Node destination = nodeRepository
                .findById(destinationId)
                .orElseThrow(() -> new NodeNotFoundException(String.format("destination node:%d not found", destinationId)));
        Assert.notNull(source.getParent(), "root node cant't move");
        treeManipulator.changeParent(source, destination);
        logger.info("parent of node with id {} is changed to the node with id {}", sourceId, destinationId);
    }

    /**
     * insert new tree in DB
     *
     * @param rootNode
     * @throws IllegalArgumentException if structure of given tree is not correct
     */
    @Transactional
    @Override
    public void insertNew(Node rootNode) {
        validateTree(rootNode, new HashSet<>());
        prepareTree(rootNode, 1, 0);
        nodeRepository.deleteTree();
        treeManipulator.saveTree(rootNode);
        logger.info("new tree was created!");

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
        logger.info("tree structure was valid.");
    }


}
